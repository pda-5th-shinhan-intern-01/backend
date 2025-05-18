package shinhan.intern.hotsignal.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shinhan.intern.hotsignal.sector.Sector;

import java.time.LocalDate;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock,Long>{
    List<Stock> findAllByTicker(String ticker);
    List<Stock> findTop2ByTickerOrderByDateDesc(String ticker);

    Stock findTopByTickerOrderByDateDesc(String ticker);
    @Query(value = """
        SELECT * FROM stock 
        WHERE ticker = :ticker 
          AND date <= :baseDate 
        ORDER BY date DESC 
        LIMIT 1 OFFSET :offset
    """, nativeQuery = true)
    Stock findBeforeDateWithOffset(@Param("ticker") String ticker,
                                   @Param("baseDate") LocalDate baseDate,
                                   @Param("offset") int offset);

    @Query(value = """
        SELECT * FROM stock 
        WHERE ticker = :ticker 
          AND date > :baseDate 
        ORDER BY date ASC 
        LIMIT 1 OFFSET :offset
    """, nativeQuery = true)
    Stock findAfterDateWithOffset(@Param("ticker") String ticker,
                                  @Param("baseDate") LocalDate baseDate,
                                  @Param("offset") int offset);

    Stock findTopByNameOrderByDateDesc(String name);

    @Query(value = """
        SELECT * FROM (
            SELECT *, ROW_NUMBER() OVER (PARTITION BY ticker ORDER BY date DESC) AS rn
            FROM stock s
            WHERE s.sector_id = :#{#sector.id}
        ) ranked
        WHERE rn = 1
    """, nativeQuery = true)
    List<Stock> findLatestStocksBySector(Sector sector);

    @Query(value = """
        SELECT * FROM (
            SELECT *, ROW_NUMBER() OVER (PARTITION BY ticker ORDER BY date DESC) AS rn
            FROM stock
        ) ranked
        WHERE rn = 1
    """, nativeQuery = true)
    List<Stock> findLatestStocks();

}
