package shinhan.intern.hotsignal.fomc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FomcService {
    private final FomcRepository fomcRepository;

    public List<FomcDTO> findFomcList(List<Long> ids) {
        List<Fomc> fomcs = (ids == null || ids.isEmpty())
                ? fomcRepository.findAll()
                : fomcRepository.findAllById(ids);

        return fomcs.stream()
                .map(f -> FomcDTO.builder()
                        .id(f.getId())
                        .title(f.getTitle())
                        .summary(f.getContent())
                        .date(f.getDate())
                        .policyBias(f.getPolicyBias())
                        .sourceUrl(f.getSourceUrl())
                        .videoUrl(f.getVideoUrl())
                        .build())
                .toList();
    }
}
