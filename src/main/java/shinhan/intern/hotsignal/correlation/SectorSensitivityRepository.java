package shinhan.intern.hotsignal.correlation;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface SectorSensitivityRepository extends JpaRepository<SectorSensitivity,Long> {
    List<SectorSensitivity> findByWindow(String window);
}
