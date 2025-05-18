package shinhan.intern.hotsignal.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockTodayDTO {
    private String name;
    private String ticker;
    private Double price;
    private Long volume;
    private Long marketCap;
}
