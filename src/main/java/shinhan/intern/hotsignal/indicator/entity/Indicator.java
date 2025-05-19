package shinhan.intern.hotsignal.indicator.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Data
@Getter
@Table(name = "indicator")
public class Indicator {
    @Id
    private Long id;

    private String name;

    private String code;

    private LocalDate date;

    private Double value;

    private Double prev;
    private Double forecast;
    private String unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "indicatormeta_id", referencedColumnName = "id", nullable = true)
    private IndicatorMeta indicatorMeta;
}
