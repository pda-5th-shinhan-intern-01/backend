package shinhan.intern.hotsignal.stock.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class StockPriceDTO {
    private LocalDate date;
    private double price;

}
