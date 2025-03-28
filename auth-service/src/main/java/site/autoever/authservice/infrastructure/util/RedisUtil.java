package site.autoever.authservice.infrastructure.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 데이터 가져오기 (Object)
     */
    public Object getData(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * 데이터 가져오기 (제네릭 타입 변환)
     */
    public <T> T getData(String key, Class<T> type) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Object data = valueOperations.get(key);
        return type.cast(data);
    }

    /**
     * 데이터 저장 (Object)
     */
    public void setData(String key, Object value) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    /**
     * 데이터 저장 + 만료 시간 지정 (Object)
     */
    public void setDataExpire(String key, Object value, long duration) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    /**
     * 데이터 저장 + 1일 만료 (Object)
     */
    public void setDataExpireOneDay(String key, Object value) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofDays(1); // 1일 = 24시간
        valueOperations.set(key, value, expireDuration);
    }

    /**
     * 데이터 삭제
     */
    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}