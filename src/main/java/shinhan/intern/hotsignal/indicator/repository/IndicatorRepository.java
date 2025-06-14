package shinhan.intern.hotsignal.indicator.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import shinhan.intern.hotsignal.indicator.entity.Indicator;

@Repository
public interface IndicatorRepository extends JpaRepository<Indicator, Long> {
    List<Indicator> findByCode(String code);

    @Query("SELECT DISTINCT i.code FROM Indicator i")
    List<String> findDistinctCodes();
    List<Indicator> findAllByCode(String code);
    List<Indicator> findByCodeOrderByDateDesc(String code);

    Indicator findTopByCodeOrderByDateDesc(String indicatorCode);
    List<Indicator> findTop10ByCodeOrderByDateDesc(String indicatorCode);
    List<Indicator>findAllByOrderByDateDesc();
}

