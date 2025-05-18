package shinhan.intern.hotsignal.sensitivity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shinhan.intern.hotsignal.stock.dto.StockChartDTO;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SensitivityChartDTO {
    private String indicatorCode;
    private String indicatorName;
    private LocalDate date;
    private Double prev;
    private Double actual;
    private Double delta;
    private String unit;
    private Double stockRate;
    private List<StockChartDTO> price;
}
