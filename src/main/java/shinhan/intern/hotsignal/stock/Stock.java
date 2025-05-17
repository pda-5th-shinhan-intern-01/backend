package shinhan.intern.hotsignal.stock;

import jakarta.persistence.*;
import lombok.*;
import shinhan.intern.hotsignal.sector.Sector;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "stock", uniqueConstraints = @UniqueConstraint(columnNames = {"ticker", "date"}))
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticker;
    private String name;

    private LocalDate date;

    @Column(name = "open_price")
    private Double openPrice;

    @Column(name = "close_price")
    private Double closePrice;

    @Column(name = "high_price")
    private Double highPrice;

    @Column(name = "low_price")
    private Double lowPrice;

    private Long volume;

    @Column(name="market_cap")
    private Long marketCap;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id")
    private Sector sector;
}
