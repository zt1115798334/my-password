package com.zt.mypassword.cache.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zt.mypassword.cache.RedisService;
import com.zt.mypassword.cache.template.JSONArrayRedisTemplate;
import com.zt.mypassword.cache.template.JSONObjectRedisTemplate;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
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
 * date: 2018/9/12 11:30
 * description:
 */
@AllArgsConstructor
@Component
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final JSONArrayRedisTemplate jsonArrayRedisTemplate;

    private final JSONObjectRedisTemplate jsonObjectRedisTemplate;

    @Override
    public Optional<Object> getObject(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return Optional.ofNullable(valueOperations.get(key));
    }

    @Override
    public Set<Object> getObjectForSet(String key) {
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        return setOperations.members(key);
    }

    @Override
    public Optional<JSONObject> getJSONObject(String key) {
        ValueOperations<String, JSONObject> valueOperations = jsonObjectRedisTemplate.opsForValue();
        return Optional.ofNullable(valueOperations.get(key));
    }

    @Override
    public List<JSONObject> getJSONObject(List<String> keys) {
        ValueOperations<String, JSONObject> valueOperations = jsonObjectRedisTemplate.opsForValue();
        return valueOperations.multiGet(keys);
    }

    @Override
    public Optional<JSONArray> getJSONArray(String key) {
        ValueOperations<String, JSONArray> valueOperations = jsonArrayRedisTemplate.opsForValue();
        return Optional.ofNullable(valueOperations.get(key));
    }

    @Override
    public void setContainExpire(String key, JSONObject value) {
        ValueOperations<String, JSONObject> valueOperations = jsonObjectRedisTemplate.opsForValue();
        valueOperations.set(key, value, 30, TimeUnit.MINUTES);
    }

    @Override
    public void setContainExpire(String key, JSONObject value, long timeout, TimeUnit unit) {
        ValueOperations<String, JSONObject> valueOperations = jsonObjectRedisTemplate.opsForValue();
        valueOperations.set(key, value, timeout, unit);
    }

    @Override
    public void setContainExpire(String key, JSONArray value, long timeout, TimeUnit unit) {
        ValueOperations<String, JSONArray> valueOperations = jsonArrayRedisTemplate.opsForValue();
        valueOperations.set(key, value, timeout, unit);
    }

    @Override
    public void setNotContainExpire(String key, JSONObject value) {
        ValueOperations<String, JSONObject> valueOperations = jsonObjectRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    @Override
    public void setNotContainExpire(String key, JSONArray value) {
        ValueOperations<String, JSONArray> valueOperations = jsonArrayRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    @Override
    public void setNotContainExpireForSet(String key, String value) {
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        setOperations.add(key, value);
    }

    @Override
    public void setNotContainExpireForSet(String key, List<String> value) {
        if (value.isEmpty()) {
            return;
        }
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        setOperations.add(key, value.toArray(new Object[0]));
    }

    @Override
    public Set<String> keys(String key) {
        return redisTemplate.keys(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void deleteLike(String key) {
        Set<String> keys = this.keys(key);
        for (String s : keys) {
            this.delete(s);
        }
    }

    @Override
    public void removeForSet(String key, String value) {
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        setOperations.remove(key, value);
    }

    @Override
    public void removeForSet(String key, List<String> value) {
        if (value.isEmpty()) {
            return;
        }
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        setOperations.remove(key, value.toArray(new Object[0]));
    }

    @Override
    public void receiveMessage(String message) {
        System.out.println("message = " + message);
    }


}
