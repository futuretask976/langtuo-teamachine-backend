package com.langtuo.teamachine.dao.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisManager {
    /**
     * 缓存过期时间
     */
    private static final long TIMEOUT = 30L;
    private static final TimeUnit TIME_UNIT_MIN = TimeUnit.MINUTES;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value, TIMEOUT, TIME_UNIT_MIN);
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
}
