package shinhan.intern.hotsignal.indicator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IndicatorDTO {
    private String name;
    private String code;
    private LocalDate date;
    private Double value;
    private String unit;
    private Double previous;
    private Double forecast;
}
