package shinhan.intern.hotsignal.sensitivity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockSensitivityRepository extends JpaRepository<StockSensitivity,Long> {
    @Query("SELECT DISTINCT s.indicatorMeta.id FROM StockSensitivity s")
    List<Long> findDistinctIndicatorMetaIds();

    List<StockSensitivity> findAllByStockId(Long id);

    @Query("SELECT ss FROM StockSensitivity ss WHERE ss.stock.ticker = :ticker")
    List<StockSensitivity> findAllByStockTicker(@Param("ticker") String ticker);

    StockSensitivity findByStock_TickerAndIndicatorMeta_Code(String ticker, String code);

    List<StockSensitivity> findTop10ByIndicatorMeta_IdOrderByScoreDesc(Long indicatorId);
}
