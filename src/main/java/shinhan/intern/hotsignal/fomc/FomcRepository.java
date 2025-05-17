package shinhan.intern.hotsignal.fomc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FomcRepository extends JpaRepository<Fomc,Long> {

}
