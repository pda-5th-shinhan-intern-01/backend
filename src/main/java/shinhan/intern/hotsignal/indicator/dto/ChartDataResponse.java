package shinhan.intern.hotsignal.indicator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChartDataResponse {
    private String date;
    private Double value;
}
