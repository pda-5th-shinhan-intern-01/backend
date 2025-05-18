package shinhan.intern.hotsignal.sensitivity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensitivityPerformanceDTO {
    private String indicatorName;
    private String indicatorCode;
    private Double prev;
    private Double forecast;
    private Double delta;
    private Double score;
    private Double performance;
    private String unit;
    private LocalDate date;
    private LocalTime time;
}
