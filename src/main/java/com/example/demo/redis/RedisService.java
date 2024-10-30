package com.example.demo.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void saveJsonAsHash(String key, Object jsonObject) {
        var map = objectMapper.convertValue(jsonObject, Map.class);
        redisTemplate.opsForHash().putAll(key, map);
    }

    public void saveJsonAsHash(String key, Object jsonObject, long timeout, TimeUnit timeUnit) {
        var map = objectMapper.convertValue(jsonObject, Map.class);
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public <T> T getJsonFromHash(String key, Class<T> clazz) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        return objectMapper.convertValue(map, clazz);
    }

    public void addToList(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public void addToListWithExpiration(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForList().rightPush(key, value);
        redisTemplate.expire(key, timeout, timeUnit);
    }

    public Object getFromList(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    public List<Object> getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}
