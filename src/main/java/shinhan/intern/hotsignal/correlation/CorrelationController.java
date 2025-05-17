package shinhan.intern.hotsignal.correlation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/correlation")
public class CorrelationController {
    private final CorrelationService correlationService;

    @GetMapping("/heatMap")
    public ResponseEntity<HeatMapDTO> getHeatMapData(@RequestParam(name="window", defaultValue = "day") String window) {
        if (!Arrays.asList("day", "1d", "3d").contains(window)) {
            return ResponseEntity.badRequest().body(null);
        }
        HeatMapDTO res = correlationService.findHeatMapData(window);

        return ResponseEntity.ok(res);
    }

}
