package shinhan.intern.hotsignal.indicator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shinhan.intern.hotsignal.indicator.entity.Indicator;

@Repository
public interface IndicatorRepository extends JpaRepository<Indicator, Long> {
    List<Indicator> findByCode(String code);
}

