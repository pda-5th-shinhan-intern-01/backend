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
    private String expectedValue;
    private String prevValue;
    private String actualValue;
    private String unit;
    private String time;
    private String country = "US";

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IndicatorDto {
        private String name;
        private String code;
    }
}
