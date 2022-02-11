package com.zt.mypassword.aop;

import com.zt.mypassword.aop.cache.CacheKeys;
import com.zt.mypassword.cache.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class RedisCacheClearAspect {

    private final RedisService redisService;

    public RedisCacheClearAspect(RedisService redisService) {
        this.redisService = redisService;
    }

    @Pointcut("execution( * com.zt.mypassword.controller..*.*(..)) && @annotation(redisCacheClear)")
    private void aopPointCut(RedisCacheClear redisCacheClear) {

    }

    @Before(value = "aopPointCut(redisCacheClear)", argNames = "redisCacheClear")
    private void doAfterReturning(RedisCacheClear redisCacheClear) {
        redisService.delete(CacheKeys.getInterKey(StringUtils.EMPTY));
    }


}
