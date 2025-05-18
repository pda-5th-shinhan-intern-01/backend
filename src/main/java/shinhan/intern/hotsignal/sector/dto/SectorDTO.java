package shinhan.intern.hotsignal.sector.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectorDTO {
    private final String sectorName;
    private final Double sectorChangeRate;
}
