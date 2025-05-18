package shinhan.intern.hotsignal.sensitivity.dto;

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
    private String phrase;
    private List<StockSensitivityDTO> topStocks;
}
