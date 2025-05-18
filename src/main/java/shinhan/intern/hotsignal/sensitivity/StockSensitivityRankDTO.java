package shinhan.intern.hotsignal.sensitivity;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockSensitivityRankDTO {
    private Long indicatorId;
    private String indicatorCode;
    private String indicatorName;
    private List<StockSensitivityDTO> topStocks;
}
