package shinhan.intern.hotsignal.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockChartDTO {
    private LocalDate date;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;
}
