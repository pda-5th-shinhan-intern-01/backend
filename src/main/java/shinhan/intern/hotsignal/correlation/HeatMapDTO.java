package shinhan.intern.hotsignal.correlation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class HeatMapDTO {
    private List<List<Double>> matrix; //[indicator][sector];
    private List<String> indicators;
    private List<String> sectors;
}
