package shinhan.intern.hotsignal.sensitivity;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shinhan.intern.hotsignal.indicator.entity.EconomicEvent;
import shinhan.intern.hotsignal.indicator.entity.Indicator;
import shinhan.intern.hotsignal.indicator.repository.EconomicEventRepository;
import shinhan.intern.hotsignal.indicator.repository.IndicatorRepository;
import shinhan.intern.hotsignal.sensitivity.dto.*;
import shinhan.intern.hotsignal.sensitivity.dto.StockSensitivityDTO;
import shinhan.intern.hotsignal.sensitivity.dto.StockSensitivityRankDTO;
import shinhan.intern.hotsignal.stock.Stock;
import shinhan.intern.hotsignal.stock.StockRepository;
import shinhan.intern.hotsignal.stock.StockService;
import shinhan.intern.hotsignal.stock.dto.StockChartDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StockSensitivityService {
    private final StockSensitivityRepository stockSensitivityRepository;
    private final StockService stockService;
    private final StockRepository stockRepository;
    private final IndicatorRepository indicatorRepository;
    private final EconomicEventRepository economicEventRepository;
    public List<StockSensitivityRankDTO> findTop10ForAllIndicators() {
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
                    .phrase(indicatorPhraseMap.get(indicator.getCode()))
                    .topStocks(stocks)
                    .build());
        }

        return result;
    }


    public List<SensitivityDTO> findAllByStockTicker(String ticker){
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

        return dtoList;
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

    public List<SensitivityChartDTO> findChartDataByStockTicker(String ticker) {
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
        return result;
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
    private static final Map<String, String> indicatorPhraseMap = Map.ofEntries(
            Map.entry("CORE_CPI", "근원 CPI에 반응한 종목은?"),
            Map.entry("CORE_PPI", "근원 PPI 발표 이후 움직임이 큰 종목은?"),
            Map.entry("CORE_PCE", "소비자 심리에 반응한 종목은?"),
            Map.entry("NFP", "고용 발표 후 움직인 종목은?"),
            Map.entry("UNEMPLOYMENT", "실업률에 민감하게 반응한 종목은?"),
            Map.entry("RETAIL_SALES", "소비지표에 반응한 대표 종목은?"),
            Map.entry("GDP", "GDP 발표 후 움직인 종목은?"),
            Map.entry("INDUSTRIAL_PRODUCTION", "산업생산 지표에 민감한 종목은?")
    );

    public List<SensitivityPerformanceDTO> findExpectedPerformance(String ticker) {
        // 가장 최근 종목 id 찾기
        Stock stock = stockRepository.findTopByTickerOrderByDateDesc(ticker);

        // 그걸로 예상 퍼포먼스 찾기
        List<StockSensitivity> sensitivities = stockSensitivityRepository.findAllByStockId(stock.getId());
        // 지표id로 정보 가져와서 제공
        List<SensitivityPerformanceDTO> result = new ArrayList<>();
        for (StockSensitivity s : sensitivities) {
            Indicator indicator = s.getIndicator();
            Optional<EconomicEvent> Oevent = economicEventRepository.findFirstByIndicatorAndDateAfterOrderByDateAsc(indicator,LocalDate.now());
            if(Oevent.isEmpty()) continue;
            EconomicEvent event = Oevent.get();
            String prevStr = event.getPrevious();    // 예: "3.2%"
            String forecastStr = event.getForecast(); // 예: "3.4%"

            // 숫자 추출
            Double prev = parseDouble(prevStr);
            Double forecast = parseDouble(forecastStr);
            Double delta = (prev != null && forecast != null) ? forecast - prev : null;
            Double beta = s.getScore();
            Double performance = (delta != null && beta != null) ? delta * beta : null;
            // 단위 추출: 맨 뒤의 비숫자 문자열
            String unit = extractUnit(forecastStr != null ? forecastStr : prevStr);

            if (performance==null) continue;
            if ("%".equals(unit) && performance != null) {
                performance = performance * 100;
            }
            result.add(SensitivityPerformanceDTO.builder()
                    .performance(performance)
                    .score(s.getScore())
                    .forecast(forecast)
                    .prev(prev)
                    .delta(delta)
                    .unit(unit)
                    .date(event.getDate())
                    .time(event.getTime())
                    .indicatorCode(indicator.getCode())
                    .indicatorName(indicator.getName())
                    .build());
        }
        result.sort(Comparator.comparing(dto -> LocalDateTime.of(
                dto.getDate(),
                dto.getTime() != null ? dto.getTime() : LocalTime.MIDNIGHT
        )));
        return result;
    }
    
    //파싱 메소드
    private Double parseDouble(String value) {
        if (value == null) return null;
        try {
            // 숫자와 소수점만 남기고 파싱
            String numeric = value.replaceAll("[^0-9.\\-]", "");
            return numeric.isEmpty() ? null : Double.parseDouble(numeric);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String extractUnit(String value) {
        if (value == null) return null;
        // 숫자와 . 만 제거 → 남는 건 단위
        return value.replaceAll("[0-9.\\-]", "").trim();
    }
}
