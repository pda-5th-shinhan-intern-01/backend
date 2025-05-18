package shinhan.intern.hotsignal.sensitivity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensitivityDTO {
    private String indicatorCode;
    private String indicatorName;
    private Double sensitivity;
    private String unit;
}
