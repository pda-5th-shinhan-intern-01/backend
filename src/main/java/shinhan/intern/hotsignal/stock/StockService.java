package shinhan.intern.hotsignal.stock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import shinhan.intern.hotsignal.stock.dto.StockChartDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;

    public List<StockChartDTO> getChartData(String ticker) {
        List<Stock> stocks = stockRepository.findAllByTicker(ticker);
        return stocks.stream()
                .map(stock -> StockChartDTO.builder()
                        .date(stock.getDate())
                        .close(stock.getClosePrice())
                        .high(stock.getHighPrice())
                        .low(stock.getLowPrice())
                        .open(stock.getOpenPrice())
                        .volume(stock.getVolume())
                        .build())
                .toList();
    }

    public Double calculateStockChange(String ticker) {
        List<Stock> closes = stockRepository.findTop2ByTickerOrderByDateDesc(ticker);
        if (closes.size() < 2) return null;

        double today = closes.get(0).getClosePrice();
        double yesterday = closes.get(1).getClosePrice();

        double rawChange = ((today - yesterday) / yesterday) * 100;

        return Math.round(rawChange * 100.0) / 100.0;
    }

    public List<Stock> get7DayPriceTrend(String ticker, LocalDate baseDate) {
        List<Stock> before = new ArrayList<>();
        for (int i = 3; i >= 1; i--) {
            Stock s = stockRepository.findBeforeDateWithOffset(ticker, baseDate, i);
            if (s != null) before.add(s);
        }

        Stock base = stockRepository.findBeforeDateWithOffset(ticker, baseDate, 0);

        List<Stock> after = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Stock s = stockRepository.findAfterDateWithOffset(ticker, baseDate, i);
            if (s != null) after.add(s);
        }

        List<Stock> result = new ArrayList<>();
        result.addAll(before);
        if (base != null) result.add(base);
        result.addAll(after);

        return result;
    }
}
