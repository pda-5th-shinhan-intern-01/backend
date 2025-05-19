package shinhan.intern.hotsignal.indicator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IndicatorInfoDTO {
    private String indicatorName;
    private String indicatorCode;
    private Double prev;
    private Double next;
    private Double delta;
    private String unit;
}
