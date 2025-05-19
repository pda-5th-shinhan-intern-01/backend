package shinhan.intern.hotsignal.indicator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shinhan.intern.hotsignal.indicator.entity.IndicatorMeta;

@Repository
public interface IndicatorMetaRepository extends JpaRepository<IndicatorMeta,Long> {
}
