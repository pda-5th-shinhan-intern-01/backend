package shinhan.intern.hotsignal.indicator.entity;

import java.time.LocalDate;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "경제이벤트")
public class EconomicEvent {
    @Id
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "forecast")
    private Double forecast;

    @Column(name = "previous")
    private Double previous;

    @Column(name = "actual")
    private Double actual;

    @ManyToOne
    @JoinColumn(name = "indicator_id") // 지표 ID 연결
    private Indicator indicator;
}

