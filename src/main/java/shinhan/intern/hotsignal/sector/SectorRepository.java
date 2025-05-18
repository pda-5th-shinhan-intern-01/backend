package shinhan.intern.hotsignal.sector;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import shinhan.intern.hotsignal.stock.Stock;

import java.util.List;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {
    Sector findByName(String sectorName);
    @Query(value = """
    SELECT s.*
    FROM stock s
    JOIN (
        SELECT ticker, MAX(date) AS latestDate
        FROM stock
        GROUP BY ticker
    ) latest
    ON s.ticker = latest.ticker AND s.date = latest.latestDate
    WHERE s.sector_id = :sectorId
""", nativeQuery = true)
    List<Stock> findLatestDataBySector(@Param("sectorId") Long sectorId);

}
