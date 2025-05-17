package shinhan.intern.hotsignal.correlation;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import shinhan.intern.hotsignal.indicator.Indicator;
import shinhan.intern.hotsignal.sector.Sector;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity
@Getter
@Table(name="sectorsensitivity")
public class SectorSensitivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Correlation correlation;

    private Double score;

    @Column(name = "`window`")
    private String window;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "indicator_id", nullable = false)
    private Indicator indicator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

}
