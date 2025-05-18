package shinhan.intern.hotsignal.sensitivity;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shinhan.intern.hotsignal.indicator.Indicator;
import shinhan.intern.hotsignal.stock.Stock;
import shinhan.intern.hotsignal.stock.StockService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StockSensitivityService {
    private final StockSensitivityRepository stockSensitivityRepository;
    private final StockService stockService;
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
                    .indicatorId(indicator.getId())
                    .indicatorCode(indicator.getCode())
                    .indicatorName(indicator.getName())
                    .topStocks(stocks)
                    .build());
        }

        return ResponseEntity.ok(result);
    }
}
