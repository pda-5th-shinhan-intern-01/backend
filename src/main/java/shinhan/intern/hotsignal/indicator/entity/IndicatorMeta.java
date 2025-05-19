package shinhan.intern.hotsignal.indicator.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name="indicatormeta")
public class IndicatorMeta {
    @Id
    private Long id;
    private String name;
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ValueType type;

    @Column(name="default_unit")
    private String unit;
}
