package shinhan.intern.hotsignal.sensitivity;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shinhan.intern.hotsignal.sensitivity.StockSensitivityService;
import shinhan.intern.hotsignal.sensitivity.dto.SensitivityChartDTO;
import shinhan.intern.hotsignal.sensitivity.dto.SensitivityDTO;
import shinhan.intern.hotsignal.sensitivity.dto.SensitivityPerformanceDTO;
import shinhan.intern.hotsignal.sensitivity.dto.StockSensitivityRankDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensitivities")
public class SensitivityController {
    private final StockSensitivityService stockSensitivityService;


    @GetMapping("/top")
    public ResponseEntity<List<StockSensitivityRankDTO>> getTopStocks() {
        List<StockSensitivityRankDTO> res = stockSensitivityService.findTop10ForAllIndicators();
        if (res == null || res.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<List<SensitivityDTO>> getSensitivities(@PathVariable String ticker) {
        List<SensitivityDTO> res = stockSensitivityService.findAllByStockTicker(ticker);
        if (res == null || res.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/rate/{ticker}")
    public ResponseEntity<List<SensitivityChartDTO>> getSensitivityChartData(@PathVariable String ticker, @RequestParam(required = false, defaultValue = "date") String sortedBy){
        List<SensitivityChartDTO> res = stockSensitivityService.findChartDataByStockTicker(ticker,sortedBy);
        if (res == null || res.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("/performance/{ticker}")
    public ResponseEntity<List<SensitivityPerformanceDTO>> getExpectedPerformance(@PathVariable String ticker, @RequestParam(required = false, defaultValue = "closet") String sortedBy){
        List<SensitivityPerformanceDTO> res = stockSensitivityService.findExpectedPerformance(ticker,sortedBy);
        if (res == null || res.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(res);
    }
}
