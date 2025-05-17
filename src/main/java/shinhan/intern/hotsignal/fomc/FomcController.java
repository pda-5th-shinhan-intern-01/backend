package shinhan.intern.hotsignal.fomc;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fomc")
public class FomcController {

    private final FomcService fomcService;

    @GetMapping("")
    public ResponseEntity<List<FomcDTO>> getFomcList(@RequestParam(name="id", required = false) List<Long> ids) {
        List<FomcDTO> res = fomcService.findFomcList(ids);
        return ResponseEntity.ok(res);
    }
}
