package shinhan.intern.hotsignal.indicator.entity;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "지표")
public class Indicator {
    @Id
    private Integer id;

    private String name;

    private String code;
}
