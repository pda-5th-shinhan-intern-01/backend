package shinhan.intern.hotsignal.sensitivity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StockSensitivityRepository extends JpaRepository<StockSensitivity,Long> {
    List<StockSensitivity> findAllByIndicatorId(Long indicatorId);
    @Query("SELECT DISTINCT s.indicator.id FROM StockSensitivity s")
    List<Long> findDistinctIndicatorIds();

    List<StockSensitivity> findTop10ByIndicatorIdOrderByPerformanceDesc(Long indicatorId);

    List<StockSensitivity> findAllByStockId(Long id);
}
