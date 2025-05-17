package shinhan.intern.hotsignal.fomc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class FomcDTO {
    private Long id;
    private String title;
    private String summary;
    private String policyBias;
    private LocalDate date;
    private String sourceUrl;
    private String videoUrl;

}
