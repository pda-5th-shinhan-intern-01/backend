package shinhan.intern.hotsignal.stock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shinhan.intern.hotsignal.stock.dto.StockChartDTO;
import shinhan.intern.hotsignal.stock.dto.StockPriceDTO;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @GetMapping("/{ticker}/chart")
    public List<StockChartDTO> getChartData(@PathVariable String ticker) {
        return stockService.getChartData(ticker);
    }
}
