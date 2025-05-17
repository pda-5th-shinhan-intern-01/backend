package shinhan.intern.hotsignal.fomc;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="summary")
@Getter
public class Fomc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;
    private LocalDate date;

    @Column(name="policy_bias")
    private String policyBias;

    @Column(name="source_url")
    private String sourceUrl;

    @Column(name="video_url")
    private String videoUrl;


}
