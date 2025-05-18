package shinhan.intern.hotsignal.stock;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock,Long>{
    List<Stock> findAllByTicker(String ticker);
    List<Stock> findTop2ByTickerOrderByDateDesc(String ticker);
}
