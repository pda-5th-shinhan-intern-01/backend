// package shinhan.intern.hotsignal.sensitivity;

// import lombok.RequiredArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import java.util.List;

// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/api/sensitivities")
// public class SensitivityController {
//     private final SectorSensitivityService sectorSensitivityService;
//     private final StockSensitivityService stockSensitivityService;

//     @GetMapping("/top")
//     public ResponseEntity<List<StockSensitivityRankDTO>> getTopStocks() {
//         return stockSensitivityService.findTop10ForAllIndicators();
//     }
// }
