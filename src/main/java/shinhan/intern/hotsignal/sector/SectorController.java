package shinhan.intern.hotsignal.sector;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shinhan.intern.hotsignal.sector.dto.SectorDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sectors")
public class SectorController {
    private final SectorService sectorService;

    @GetMapping("")
    public ResponseEntity<List<SectorDTO>> getAllSector (){
        return sectorService.findAllSectors();
    }
}
