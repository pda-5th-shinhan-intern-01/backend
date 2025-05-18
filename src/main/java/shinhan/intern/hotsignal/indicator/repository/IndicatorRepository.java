package shinhan.intern.hotsignal.indicator.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import shinhan.intern.hotsignal.indicator.entity.Indicator;

@Repository
public interface IndicatorRepository extends JpaRepository<Indicator, Integer> {
    Optional<Indicator> findByCode(String code);
    List<Indicator> findAllByCode(String code);
}

