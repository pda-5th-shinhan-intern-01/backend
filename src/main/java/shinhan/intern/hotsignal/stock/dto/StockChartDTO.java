package shinhan.intern.hotsignal.stock.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class StockChartDTO {
    private String date;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;
}
