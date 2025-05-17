package shinhan.intern.hotsignal.sector;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name="sector")
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(name="change_rate")
    private Double changeRate;

    @Column(name="created_at")
    private LocalDateTime createdAt;
}
