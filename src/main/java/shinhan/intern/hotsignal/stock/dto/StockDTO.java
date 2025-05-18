package shinhan.intern.hotsignal.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shinhan.intern.hotsignal.sector.dto.SectorDTO;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockDTO {
    private String name;
    private String ticker;
    private Double currentPrice;
    private Double changeRate;
    private Long marketCap;
    private Long volume;
    private SectorDTO sector;
}