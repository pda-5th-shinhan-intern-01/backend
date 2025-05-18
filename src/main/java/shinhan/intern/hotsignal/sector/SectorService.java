package shinhan.intern.hotsignal.sector;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shinhan.intern.hotsignal.sector.dto.SectorDTO;
import shinhan.intern.hotsignal.sector.dto.SectorDetailDTO;
import shinhan.intern.hotsignal.stock.Stock;
import shinhan.intern.hotsignal.stock.StockService;
import shinhan.intern.hotsignal.stock.dto.StockTodayDTO;


import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SectorService {
    private final SectorRepository sectorRepository;
    private final StockService stockService;

    public ResponseEntity<List<SectorDTO>> findAllSectors() {
        List<Sector> sectors = sectorRepository.findAll();
        List<SectorDTO> result = sectors.stream()
                .map(s -> SectorDTO.builder()
                        .sectorName(s.getName())
                        .sectorChangeRate(s.getChangeRate()*100)
                        .build())
                .toList();
        return ResponseEntity.ok(result);
    }

    public ResponseEntity<SectorDetailDTO> findSectorStocks(String sectorName) {
        Sector sector = sectorRepository.findByName(sectorName);
        SectorDetailDTO res = new SectorDetailDTO();
        res.setSectorName(sectorName);
        res.setSectorEName(sectorNameToEname.getOrDefault(sectorName, "Unknown"));
        res.setSectorDescription(sectorIntroMap.getOrDefault(sectorName, "Unknown"));
        res.setSectorChangeRate(sector.getChangeRate()*100);
        List<Stock> stocks = sectorRepository.findLatestDataBySector(sector.getId());
        List<StockTodayDTO> st = stocks.stream()
                .map(s->StockTodayDTO.builder()
                        .price(s.getClosePrice())
                        .name(s.getName())
                        .ticker(s.getTicker())
                        .volume(s.getVolume())
                        .marketCap(s.getMarketCap())
                        .changeRate(stockService.calculateStockChange(s.getTicker()))
                        .build())
                .toList();
        res.setStocks(st);
        return ResponseEntity.ok(res);
    }

    private static final Map<String, String> sectorNameToEname = Map.ofEntries(
            Map.entry("기술", "Information Technology"),
            Map.entry("금융", "Financials"),
            Map.entry("헬스케어", "Health Care"),
            Map.entry("자유소비재", "Consumer Discretionary"),
            Map.entry("필수소비재", "Consumer Staples"),
            Map.entry("에너지", "Energy"),
            Map.entry("산업재", "Industrials"),
            Map.entry("소재", "Materials"),
            Map.entry("부동산", "Real Estate"),
            Map.entry("유틸리티", "Utilities"),
            Map.entry("커뮤니케이션", "Communication Services")
    );

    private static final Map<String, String> sectorIntroMap = Map.ofEntries(
            Map.entry("기술", "소프트웨어, 하드웨어, 반도체, IT 서비스를 포함하는 섹터"),
            Map.entry("금융", "은행, 보험, 자산관리, 금융 서비스를 포함하는 섹터"),
            Map.entry("헬스케어", "제약, 바이오테크, 의료기기, 의료서비스 등을 포함하는 섹터"),
            Map.entry("자유소비재", "자동차, 가전, 유통, 레저 등 경기 민감 소비를 포함하는 섹터"),
            Map.entry("필수소비재", "식품, 음료, 생활필수품 등 기본 소비를 포함하는 섹터"),
            Map.entry("에너지", "석유, 가스, 정유, 에너지 장비 및 서비스 산업 포함"),
            Map.entry("산업재", "건설, 기계, 물류, 항공 등 제조 기반 산업"),
            Map.entry("소재", "화학, 금속, 자원, 포장 등 원자재 관련 산업"),
            Map.entry("부동산", "상업용·주거용 부동산 및 리츠 산업 포함"),
            Map.entry("유틸리티", "전기, 가스, 수도 등 공공 서비스 제공 산업"),
            Map.entry("커뮤니케이션", "통신, 미디어, 콘텐츠, 엔터테인먼트 산업 포함")
    );

}
