package shinhan.intern.hotsignal.indicator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndicatorEventResponse {
    private String name;
    private IndicatorDto indicator;
    private String date;
    private Double expectedValue;
    private Double prevValue;
    private Double actualValue;
    private String unit;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IndicatorDto {
        private String name;
        private String code;
    }
}
