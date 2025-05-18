package shinhan.intern.hotsignal.sector.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shinhan.intern.hotsignal.stock.dto.StockTodayDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SectorDetailDTO {
    private String sectorName;
    private String sectorEName;
    private Double sectorChangeRate;
    private String sectorDescription;
    private List<StockTodayDTO> stocks;
}
