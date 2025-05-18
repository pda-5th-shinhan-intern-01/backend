package shinhan.intern.hotsignal.stock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import shinhan.intern.hotsignal.sector.Sector;
import shinhan.intern.hotsignal.sector.SectorRepository;
import shinhan.intern.hotsignal.sector.dto.SectorDTO;
import shinhan.intern.hotsignal.stock.dto.StockChartDTO;
import shinhan.intern.hotsignal.stock.dto.StockDTO;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository stockRepository;
    private final SectorRepository sectorRepository;

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
    public StockDTO getStockByTicker(String ticker) {
        Stock stock = stockRepository.findTopByTickerOrderByDateDesc(ticker);
        if (stock == null) {
            throw new EntityNotFoundException("해당 ticker의 종목이 존재하지 않습니다.");
        }

        Sector sectorEntity = stock.getSector();
        SectorDTO sector = sectorEntity != null
                ? SectorDTO.builder()
                .sectorChangeRate(sectorEntity.getChangeRate()*100)
                .sectorName(sectorEntity.getName())
                .build()
                : null;

        return StockDTO.builder()
                .changeRate(calculateStockChange(ticker))
                .currentPrice(stock.getClosePrice())
                .marketCap(stock.getMarketCap())
                .name(stock.getName())
                .sector(sector)
                .ticker(stock.getTicker())
                .volume(stock.getVolume())
                .build();
    }

    public StockDTO getStockByName(String name) {
        Stock stock = stockRepository.findTopByNameOrderByDateDesc(name);
        if (stock == null) {
            throw new EntityNotFoundException("해당 이름의 종목이 존재하지 않습니다.");
        }

        Sector sectorEntity = stock.getSector();
        SectorDTO sector = sectorEntity != null
                ? SectorDTO.builder()
                .sectorChangeRate(sectorEntity.getChangeRate()*100)
                .sectorName(sectorEntity.getName())
                .build()
                : null;

        return StockDTO.builder()
                .changeRate(calculateStockChange(stock.getTicker()))
                .currentPrice(stock.getClosePrice())
                .marketCap(stock.getMarketCap())
                .name(stock.getName())
                .sector(sector)
                .ticker(stock.getTicker())
                .volume(stock.getVolume())
                .build();
    }

    public List<StockDTO> getFilteredStocks(String sectorName, String sortedBy) {
        Sector sector = sectorName != null ? sectorRepository.findByName(sectorName) : null;

        List<Stock> stocks = (sector != null)
                ? stockRepository.findLatestStocksBySector(sector)
                : stockRepository.findLatestStocks(); // sector가 null일 땐 전체 조회하도록 별도 쿼리 필요

        Map<String, Double> changeRateMap = new HashMap<>();
        for (Stock stock : stocks) {
            String ticker = stock.getTicker();
            double changeRate = calculateStockChange(ticker); // 내부 DB 접근이 있다면 최적화 대상
            changeRateMap.put(ticker, changeRate);
        }

        Comparator<Stock> comparator = switch (sortedBy) {
            case "volume" -> Comparator.comparing(Stock::getVolume, Comparator.nullsLast(Long::compare));
            case "changeRate" -> Comparator.comparing(
                    s -> changeRateMap.getOrDefault(s.getTicker(), 0.0),
                    Comparator.nullsLast(Double::compare)
            );
            case "marketCap" -> Comparator.comparing(Stock::getMarketCap, Comparator.nullsLast(Long::compare));
            default -> Comparator.comparing(Stock::getMarketCap, Comparator.nullsLast(Long::compare));
        };

        stocks.sort(comparator.reversed());

        return stocks.stream()
                .map(stock -> StockDTO.builder()
                        .name(stock.getName())
                        .ticker(stock.getTicker())
                        .currentPrice(stock.getClosePrice())
                        .changeRate(changeRateMap.getOrDefault(stock.getTicker(), 0.0))
                        .marketCap(stock.getMarketCap())
                        .volume(stock.getVolume())
                        .sector(stock.getSector() != null
                                ? SectorDTO.builder()
                                .sectorName(stock.getSector().getName())
                                .sectorChangeRate(stock.getSector().getChangeRate()*100)
                                .build()
                                : null)
                        .build())
                .collect(Collectors.toList());
    }
}
