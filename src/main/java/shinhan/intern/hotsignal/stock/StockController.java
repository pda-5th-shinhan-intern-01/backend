package shinhan.intern.hotsignal.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shinhan.intern.hotsignal.stock.dto.StockChartDTO;
import shinhan.intern.hotsignal.stock.dto.StockDTO;
import shinhan.intern.hotsignal.stock.dto.StockTodayDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @GetMapping("/{ticker}/chart")
    public ResponseEntity<List<StockChartDTO>> getChartData(@PathVariable String ticker) {
        return ResponseEntity.ok(stockService.getChartData(ticker));
    }

    @GetMapping("")
    public ResponseEntity<List<StockDTO>> getStockData(@RequestParam(required = false) String ticker,
                                                       @RequestParam(required = false) String name,
                                                       @RequestParam(required = false, defaultValue = "market_cap") String sortedBy,
                                                       @RequestParam(required = false) String sectorName){
        List<StockDTO> stocks;
        if (ticker != null && !ticker.isEmpty()) {
            StockDTO stock = stockService.getStockByTicker(ticker);
            stocks = stock != null ? List.of(stock) : List.of();
        }
        // name으로 단건 조회
        else if (name != null && !name.isEmpty()) {
            StockDTO stock = stockService.getStockByName(name);
            stocks = stock != null ? List.of(stock) : List.of();
        }
        // 전체 or 필터 조회
        else {
            stocks = stockService.getFilteredStocks(sectorName, sortedBy);
        }
        return ResponseEntity.ok(stocks);
    }

}
