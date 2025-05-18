package shinhan.intern.hotsignal.indicator.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import shinhan.intern.hotsignal.indicator.entity.EconomicEvent;
import shinhan.intern.hotsignal.indicator.entity.Indicator;

@Repository
public interface EconomicEventRepository extends JpaRepository<EconomicEvent, Long> {
    @Query("SELECT e FROM EconomicEvent e JOIN FETCH e.indicator")
    List<EconomicEvent> findAllWithIndicator();

    Optional<EconomicEvent> findFirstByIndicatorAndDateAfterOrderByDateAsc(Indicator indicator, LocalDate today);

}

