package shinhan.intern.hotsignal.sector;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shinhan.intern.hotsignal.sector.dto.SectorDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectorService {
    private final SectorRepository sectorRepository;

    public ResponseEntity<List<SectorDTO>> findAllSectors() {
        List<Sector> sectors = sectorRepository.findAll();
        List<SectorDTO> result = sectors.stream()
                .map(s -> SectorDTO.builder()
                        .sectorName(s.getName())
                        .sectorChangeRate(s.getChangeRate()*100)
                        .build())
                .toList();
        return ResponseEntity.ok(result);
    }
}
