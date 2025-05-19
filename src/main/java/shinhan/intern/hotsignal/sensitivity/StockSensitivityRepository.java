package shinhan.intern.hotsignal.sensitivity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockSensitivityRepository extends JpaRepository<StockSensitivity,Long> {
    @Query("SELECT DISTINCT s.indicatorMeta.id FROM StockSensitivity s")
    List<Long> findDistinctIndicatorIds();

    List<StockSensitivity> findTop10ByIndicatorMeta_IdOrderByPerformanceDesc(Long indicatorId);

    List<StockSensitivity> findAllByStockId(Long id);

    StockSensitivity findByStock_TickerAndIndicatorMeta_Code(String ticker, String code);

}
