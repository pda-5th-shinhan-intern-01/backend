package shinhan.intern.hotsignal.indicator.entity;

import java.time.LocalDate;
import java.time.LocalTime;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "economicevent")
public class EconomicEvent {
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private LocalDate date;

    @Column(name="time")
    private LocalTime time;

    @Column(name = "forecast")
    private String forecast;

    @Column(name = "previous")
    private String previous;

    @Column(name = "actual")
    private String actual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "indicatormeta_id", referencedColumnName = "id", nullable = true)
    private IndicatorMeta indicatorMeta;
}

