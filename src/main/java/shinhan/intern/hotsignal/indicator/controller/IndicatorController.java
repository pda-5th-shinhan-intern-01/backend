package shinhan.intern.hotsignal.indicator.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import shinhan.intern.hotsignal.indicator.dto.IndicatorEventResponse;
import shinhan.intern.hotsignal.indicator.service.IndicatorService;

@RestController
@RequestMapping("/api/indicators")
@RequiredArgsConstructor
public class IndicatorController {
    private final IndicatorService indicatorService;

    @GetMapping
    public ResponseEntity<List<IndicatorEventResponse>> getIndicators() {
        return ResponseEntity.ok(indicatorService.getIndicatorEvents());
    }
}

