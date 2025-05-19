package shinhan.intern.hotsignal.indicator.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


import lombok.RequiredArgsConstructor;
import shinhan.intern.hotsignal.indicator.dto.ChartDataResponse;
import shinhan.intern.hotsignal.indicator.dto.IndicatorDTO;
import shinhan.intern.hotsignal.indicator.dto.IndicatorInfoDTO;
import shinhan.intern.hotsignal.indicator.dto.IndicatorEventResponse;
import shinhan.intern.hotsignal.indicator.service.IndicatorService;


@RestController
@RequestMapping("/api/indicators")
@RequiredArgsConstructor
public class IndicatorController {
    private final IndicatorService indicatorService;

    @GetMapping
    public ResponseEntity<List<IndicatorEventResponse>> getIndicators(
        @RequestParam(required = false) Long indicator_id) {
    
        if (indicator_id == null) {
            return ResponseEntity.ok(indicatorService.getIndicatorEvents());
        } else {
            return ResponseEntity.ok(indicatorService.getIndicatorEventsByIndicatorId(indicator_id));
        }
    }

    @GetMapping("/calendar")
    public ResponseEntity<List<IndicatorEventResponse>> getEvents(){
        return ResponseEntity.ok(indicatorService.getAllEconomicEvents());
    }

    @GetMapping("/{indicatorCode}/chart")
    public ResponseEntity<List<ChartDataResponse>> getIndicatorChart(@PathVariable String indicatorCode) {
        return ResponseEntity.ok(indicatorService.getIndicatorChartData(indicatorCode));
    }

    @GetMapping("/latest/{indicatorCode}")
    public ResponseEntity<IndicatorInfoDTO> getIndicatorLatest(@PathVariable String indicatorCode) {
        return ResponseEntity.ok(indicatorService.getIndicatorLatest(indicatorCode));
    }

    @GetMapping("/all")
    public ResponseEntity<List<IndicatorDTO>> getAllIndicatorList(){
        return ResponseEntity.ok(indicatorService.getAllIndicators());
    }
}

