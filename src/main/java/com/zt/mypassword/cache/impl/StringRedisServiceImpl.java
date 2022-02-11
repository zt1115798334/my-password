package com.zt.mypassword.cache.impl;

import com.zt.mypassword.cache.StringRedisService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/13 10:48
 * description:
 */
@Slf4j
@AllArgsConstructor
@Component
public class StringRedisServiceImpl implements StringRedisService {

    private final StringRedisTemplate stringRedisTemplate;


    @Override
    public void incrementNotContainExpire(String key, long delta) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.increment(key, delta);
    }

    @Override
    public void increment(String key, long delta) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.increment(key, delta);
    }

    @Override
    public void increment(String key, long delta, long timeout, TimeUnit unit) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.increment(key, delta);
        stringRedisTemplate.expire(key, timeout, unit);
    }

    @Override
    public void expire(String key, String value, long timeout, TimeUnit unit) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
        stringRedisTemplate.expire(key, timeout, unit);
    }

    @Override
    public void setNotContainExpire(String key, String value) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    @Override
    public void setContainExpire(String key, String value, long timeout, TimeUnit unit) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value, timeout, unit);
    }

    @Override
    public Optional<String> get(String key) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String s = valueOperations.get(key);
        return Optional.ofNullable(s);
    }

    @Override
    public Long expireTime(String key) {
        return stringRedisTemplate.getExpire(key);
    }


    @Override
    public Set<String> keys(String key) {
        return stringRedisTemplate.keys(key);
    }

    @Override
    public List<String> multiGet(List<String> keys) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.multiGet(keys);
    }

    @Override
    public void delete(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public void deleteByLike(String key) {
        Set<String> keys = this.keys(key + "*");
        stringRedisTemplate.delete(keys);
    }

}
