package com.zt.mypassword.cache;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/12 11:29
 * description: redis业务层
 */
public interface RedisService {


    /**
     * 根据key查询
     *
     * @param key key
     * @return Optional<JSONObject>
     */
    Optional<Object> getObject(String key);
    /**
     * 根据key查询
     *
     * @param key key
     * @return Set<JSONObject>
     */
    Set<Object> getObjectForSet(String key);
    /**
     * 根据key查询
     *
     * @param key key
     * @return Optional<JSONObject>
     */
    Optional<JSONObject> getJSONObject(String key);

    /**
     * 根据key查询
     *
     * @param keys key
     * @return Optional<JSONArray>
     */
    List<JSONObject> getJSONObject(List<String> keys);
    /**
     * 根据key查询
     *
     * @param key key
     * @return Optional<JSONArray>
     */
    Optional<JSONArray> getJSONArray(String key);

    /**
     * 保存 包含过期时间 默认 30分钟
     *
     * @param key   key
     * @param value value
     */
    void setContainExpire(String key, JSONObject value);

    /**
     * 保存 包含过期时间
     *
     * @param key     key
     * @param value   value
     * @param timeout 缓存时间
     * @param unit    缓存时间单位
     */
    void setContainExpire(String key, JSONObject value, long timeout, TimeUnit unit);

    /**
     * 保存 包含过期时间
     *
     * @param key     key
     * @param value   value
     * @param timeout 缓存时间
     * @param unit    缓存时间单位
     */
    void setContainExpire(String key, JSONArray value, long timeout, TimeUnit unit);

    /**
     * 保存 不包含过期时间
     *
     * @param key   key
     * @param value value
     */
    void setNotContainExpire(String key, JSONObject value);

    /**
     * 保存 不包含过期时间
     *
     * @param key   key
     * @param value value
     */
    void setNotContainExpire(String key, JSONArray value);

    /**
     * 保存 不包含过期时间
     *
     * @param key   key
     * @param value value
     */
    void setNotContainExpireForSet(String key, String value);

    /**
     * 保存 不包含过期时间
     *
     * @param key   key
     * @param value value
     */
    void setNotContainExpireForSet(String key, List<String> value);

    /**
     * keys
     *
     * @param key key
     * @return Set<String>
     */
    Set<String> keys(String key);

    /**
     * 删除
     *
     * @param key key
     */
    void delete(String key);

    void deleteLike(String key);

    void removeForSet(String key, String value);

    void removeForSet(String key, List<String> value);

    /**
     * 这里是收到通道的消息之后执行的方法
     *
     * @param message msg
     */
    void receiveMessage(String message);



}
