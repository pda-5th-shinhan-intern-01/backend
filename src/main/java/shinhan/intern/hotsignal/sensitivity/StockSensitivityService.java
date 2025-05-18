package shinhan.intern.hotsignal.sensitivity;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shinhan.intern.hotsignal.indicator.entity.Indicator;
import shinhan.intern.hotsignal.indicator.repository.IndicatorRepository;
import shinhan.intern.hotsignal.sensitivity.dto.SensitivityChartDTO;
import shinhan.intern.hotsignal.sensitivity.dto.SensitivityDTO;
import shinhan.intern.hotsignal.sensitivity.dto.StockSensitivityDTO;
import shinhan.intern.hotsignal.sensitivity.dto.StockSensitivityRankDTO;
import shinhan.intern.hotsignal.stock.Stock;
import shinhan.intern.hotsignal.stock.StockRepository;
import shinhan.intern.hotsignal.stock.StockService;
import shinhan.intern.hotsignal.stock.dto.StockChartDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StockSensitivityService {
    private final StockSensitivityRepository stockSensitivityRepository;
    private final StockService stockService;
    private final StockRepository stockRepository;
    private final IndicatorRepository indicatorRepository;

    public ResponseEntity<List<StockSensitivityRankDTO>> findTop10ForAllIndicators() {
        List<Long> indicatorIds = stockSensitivityRepository.findDistinctIndicatorIds();
        List<StockSensitivityRankDTO> result = new ArrayList<>();

        for (Long indicatorId : indicatorIds) {
            List<StockSensitivity> top10 = stockSensitivityRepository
                    .findTop10ByIndicatorIdOrderByPerformanceDesc(indicatorId);
            Indicator indicator = top10.get(0).getIndicator(); // 모든 항목 동일

            List<StockSensitivityDTO> stocks = top10.stream()
                    .map(s -> {
                        Stock stock = s.getStock();
                        return StockSensitivityDTO.builder()
                                .stockId(stock.getId())
                                .stockName(stock.getName())
                                .stockTicker(stock.getTicker())
                                .sensitivity(s.getScore())
                                .stockPrice(stock.getClosePrice())
                                .stockChange(stockService.calculateStockChange(stock.getTicker()))
                                .build();
                    })
                    .toList();

            result.add(StockSensitivityRankDTO.builder()
                    .indicatorId(Long.valueOf(indicator.getId()))
                    .indicatorCode(indicator.getCode())
                    .indicatorName(indicator.getName())
                    .topStocks(stocks)
                    .build());
        }

        return ResponseEntity.ok(result);
    }


    public ResponseEntity<List<SensitivityDTO>> findAllByStockTicker(String ticker){
        Stock stock = stockRepository.findTopByTickerOrderByDateDesc(ticker);
        List<StockSensitivity> sensitivities = stockSensitivityRepository.findAllByStockId(stock.getId());
        List<SensitivityDTO> dtoList = sensitivities.stream()
                .map(s -> SensitivityDTO.builder()
                        .indicatorCode(s.getIndicator().getCode())
                        .indicatorName(s.getIndicator().getName())
                        .sensitivity(s.getScore())
                        .unit(resolveUnit(s.getIndicator().getCode(), s.getUnit()))
                        .build()
                ).toList();

        return ResponseEntity.ok(dtoList);
    }
    private String resolveUnit(String code, String rawUnit) {
        if (rawUnit != null && !rawUnit.isBlank()) return rawUnit;

        return switch (code.toUpperCase()) {
            case "CPI", "PPI", "CORE_PCE", "GDP", "UNEMPLOYMENT",
                 "RETAIL_SALES", "CORE_CPI", "CORE_PPI", "INDUSTRIAL_PRODUCTION" -> "%";
            case "ISM" -> "";
            case "NFP", "PAYROLL" -> "K";
            default -> "unknown";
        };
    }

    public ResponseEntity<List<SensitivityChartDTO>> findChartDataByStockTicker(String ticker) {
        List<String> codes = indicatorRepository.findDistinctCodes();
        List<SensitivityChartDTO> result = new ArrayList<>();

        for (String code : codes) {
            List<Indicator> indicators = indicatorRepository.findByCodeOrderByDateDesc(code);
            if (indicators.size() < 2) continue;

            Indicator latest = indicators.get(0);
            Indicator previous = indicators.get(1);
            Double delta = latest.getValue() - previous.getValue();

            List<Stock> trend = stockService.get7DayPriceTrend(ticker, latest.getDate());
            Double rate = calculateReturnRate(trend, latest.getDate());
            List<StockChartDTO> charts = trend.stream()
                            .map(s -> StockChartDTO.builder()
                                    .date(s.getDate())
                                    .low(s.getLowPrice())
                                    .close(s.getClosePrice())
                                    .high(s.getHighPrice())
                                    .open(s.getOpenPrice())
                                    .volume(s.getVolume())
                                    .build())
                            .collect(Collectors.toList());

            result.add(SensitivityChartDTO.builder()
                    .indicatorCode(code)
                    .indicatorName(latest.getName())
                    .date(latest.getDate())
                    .prev(previous.getValue())
                    .actual(latest.getValue())
                    .delta(delta)
                    .unit(resolveUnit(latest.getCode(),""))
                    .stockRate(rate)
                    .price(charts)
                    .build());
        }
        return ResponseEntity.ok(result);
    }

    public Double calculateReturnRate(List<Stock> trend, LocalDate eventDate) {
        // 날짜 기준으로 정렬 (혹시 정렬 안돼있을 경우 대비)
        trend.sort(Comparator.comparing(Stock::getDate));

        Stock base = null;
        Stock end = null;

        for (Stock stock : trend) {
            if (stock.getDate().isEqual(eventDate)) {
                base = stock;
            } else if (stock.getDate().isAfter(eventDate)) {
                end = stock;
            }
        }

        // 예외 처리
        if (base == null || end == null || base.getClosePrice() == 0.0) {
            return null; // 데이터 부족 혹은 0으로 나눌 수 없음
        }

        // 등락률 = (종료일 종가 - 기준일 종가) / 기준일 종가
        return (end.getClosePrice() - base.getClosePrice()) / base.getClosePrice();
    }

}
