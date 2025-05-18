package shinhan.intern.hotsignal.sensitivity;

import jakarta.persistence.*;
import lombok.Getter;
import shinhan.intern.hotsignal.indicator.Indicator;
import shinhan.intern.hotsignal.stock.Stock;

import java.time.LocalDate;

@Getter
@Entity
@Table(name="stocksensitivity")
public class StockSensitivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Double id;

    private Double score;
    private Double performance;

    @Column(name="created_at")
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id") // FK 컬럼명
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "indicator_id") // FK 컬럼명
    private Indicator indicator;
}
