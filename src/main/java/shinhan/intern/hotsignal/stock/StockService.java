package shinhan.intern.hotsignal.stock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import shinhan.intern.hotsignal.stock.dto.StockChartDTO;

import java.util.List;

@Service
@AllArgsConstructor
public class StockService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<StockChartDTO> getChartData(String ticker) {
        String key = "stock:" + ticker;
        String cached = redisTemplate.opsForValue().get(key);
        if (cached == null) {
            throw new RuntimeException("종목 주가 데이터가 없습니다.");
        }

        try {
            return objectMapper.readValue(cached, new TypeReference<List<StockChartDTO>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Redis 데이터 파싱 실패", e);
        }
    }
}
