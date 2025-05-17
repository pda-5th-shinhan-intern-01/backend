package shinhan.intern.hotsignal.correlation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CorrelationService {
    private final SectorSensitivityRepository sectorSensitivityRepository;

    public HeatMapDTO findHeatMapData(String window) {
        List<SectorSensitivity> data = sectorSensitivityRepository.findByWindow(window);
        HeatMapDTO res = toMatrix(data);
        return res;
    }

    public HeatMapDTO toMatrix(List<SectorSensitivity> data) {
        List<String> indicators = data.stream()
                .map(s -> s.getIndicator().getCode())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        List<String> sectors = data.stream()
                .map(s -> s.getSector().getName())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        Map<String, Map<String, Double>> grouped = new HashMap<>();
        for (SectorSensitivity item : data) {
            grouped
                    .computeIfAbsent(item.getIndicator().getCode(), k -> new HashMap<>())
                    .put(item.getSector().getName(), item.getScore());
        }

        List<List<Double>> matrix = new ArrayList<>();
        for (String indicator : indicators) {
            List<Double> row = new ArrayList<>();
            for (String sector : sectors) {
                Double value = Optional.ofNullable(grouped.get(indicator))
                        .map(m -> m.get(sector))
                        .orElse(0.0); // 또는 null
                row.add(value);
            }
            matrix.add(row);
        }

        return HeatMapDTO.builder()
                .matrix(matrix)
                .indicators(indicators)
                .sectors(sectors)
                .build();
    }
}
