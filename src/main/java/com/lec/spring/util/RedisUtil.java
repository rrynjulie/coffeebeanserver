package com.lec.spring.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import java.time.Duration;

// Redis 는 데이터를 메모리에 저장하는 방식
// 응답 속도가 빠르다
@RequiredArgsConstructor
@Service
public class RedisUtil {

    private final StringRedisTemplate template;

    // Redis 에서 키값을 가지고 온다.
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    // Redis 에 키가 존재하는지 확인한다.
    public boolean existData(String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    // Redis 에 데이터를 저장하고 만료시간을 설정한다.
    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    // Redis 키에 해당하는 데이터를 Redis 에서 삭제한다.
    public void deleteData(String key) {
        template.delete(key);
    }
}
