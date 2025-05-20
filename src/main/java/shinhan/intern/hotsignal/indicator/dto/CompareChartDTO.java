package shinhan.intern.hotsignal.indicator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompareChartDTO {
    private String indicatorCode;
    private String indicatorName;
    List<CompareDTO> value;

    private String unit;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompareDTO{
        private LocalDate date;

        private Double actual;
        private Double expected;
    }

}
