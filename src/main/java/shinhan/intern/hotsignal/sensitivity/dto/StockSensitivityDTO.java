package shinhan.intern.hotsignal.sensitivity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StockSensitivityDTO {
    private Long stockId;
    private String stockName;
    private String stockTicker;
    private Double sensitivity;
    private Double stockPrice;
    private Double stockChange;
}
