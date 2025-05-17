package shinhan.intern.hotsignal.indicator.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "지표")
public class Indicator {
    @Id
    private Integer id;

    private String name;

    private String code;
}
