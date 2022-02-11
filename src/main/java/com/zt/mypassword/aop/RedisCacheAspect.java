package com.zt.mypassword.aop;

import com.alibaba.fastjson.JSONObject;
import com.zt.mypassword.aop.cache.CacheKeys;
import com.zt.mypassword.aop.utils.AopUtils;
import com.zt.mypassword.base.controller.ResultMessage;
import com.zt.mypassword.cache.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Slf4j
@Component
public class RedisCacheAspect {

    private final RedisService redisService;

    public RedisCacheAspect(RedisService redisService) {
        this.redisService = redisService;
    }

    @Pointcut("execution( * com.zt.mypassword.controller..*.*(..)) && @annotation(redisCache)")
    private void aopPointCut(RedisCache redisCache) {

    }

    @Around(value = "aopPointCut(redisCache)", argNames = "point,redisCache")
    public ResultMessage doAround(ProceedingJoinPoint point, RedisCache redisCache) throws Throwable {
        log.info("<====== 进入 redisCache 环绕通知 ======>");
        // result是方法的最终返回结果
        ResultMessage result;

        // 得到类名、方法名和参数
        Object[] args = point.getArgs();

        //注解信息 key
        String key = redisCache.key();

        //是否转换成md5值
        boolean keyTransformMd5 = redisCache.keyTransformMd5();
        //----------------------------------------------------------
        // 用SpEL解释key值
        //----------------------------------------------------------
        //解析EL表达式后的的redis的值
        String keyVal = AopUtils.parseKey(key, point, keyTransformMd5);

        // 获取目标对象
        //这块是全路径包名+目标对象名 ，默认的前缀，防止有的开发人员乱使用key，乱定义key的名称，导致重复key，这样在这加上前缀了，就不会重复使用key
        Signature signature = point.getSignature();
        String targetClassName = signature.getDeclaringTypeName() + "." + signature.getName();

        //最终的redis的key
        final String redis_final_key = CacheKeys.getInterKey(targetClassName + keyVal);
        Optional<JSONObject> valueOpt = redisService.getJSONObject(redis_final_key);

        if (valueOpt.isPresent()) { //这块是判空
            // 缓存命中，这块没用log输出，可以自定义输出
            log.info(redis_final_key + "命中缓存，得到数据");
            //拿到数据格式
            result = valueOpt.get().toJavaObject(ResultMessage.class);
        } else {
            // 缓存未命中，这块没用log输出，可以自定义输出
            log.info(redis_final_key + "缓存未命中缓存");
            // 如果redis没有数据则执行拦截的方法体
            result = JSONObject.parseObject(JSONObject.toJSONString(point.proceed(args)), ResultMessage.class);
            if (result.getMeta().isSuccess()) {
                //存入json格式字符串到redis里
                // 序列化结果放入缓存
                redisService.setContainExpire(redis_final_key, JSONObject.parseObject(JSONObject.toJSONString(result)), redisCache.expired(), redisCache.timeUnit());
            }

        }
        return result;
    }

}
