package com.zt.mypassword.aop;

import com.zt.mypassword.aop.utils.AopUtils;
import com.zt.mypassword.cache.StringRedisService;
import com.zt.mypassword.exception.custom.OperationException;
import com.zt.mypassword.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Aspect
@Slf4j
@Component
public class ThreadSleepAspect {

    private final StringRedisService stringRedisService;

    public ThreadSleepAspect(StringRedisService stringRedisService) {
        this.stringRedisService = stringRedisService;
    }

    @Pointcut("execution( * com.zt.mypassword.controller..*.*(..)) && @annotation(threadSleep)")
    private void aopPointCut(ThreadSleep threadSleep) {

    }

    @Before(value = "aopPointCut(threadSleep)", argNames = "point,threadSleep")
    private void doBefore(JoinPoint point, ThreadSleep threadSleep) {

        log.info("<====== 进入 threadSleep 环绕通知 ======>");
        //注解信息 key
        String key = threadSleep.key();
        //是否转换成md5值
        boolean keyTransformMd5 = threadSleep.keyTransformMd5();
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
        final String redisFinalKey = "thread_sleep" + ":" + targetClassName + keyVal;
        Optional<String> valueOpt = stringRedisService.get(redisFinalKey);
        if (valueOpt.isPresent()) { //这块是判空
            Long expireTime = stringRedisService.expireTime(redisFinalKey);
            throw new OperationException("当前请求相当消耗资源，请" + DateUtils.printHourMinuteSecond(expireTime) + "后重新尝试。");
        } else {
            //存入json格式字符串到redis里
            // 序列化结果放入缓存
            stringRedisService.setContainExpire(redisFinalKey, "1", Duration.of(threadSleep.expired(), threadSleep.timeUnit()));
        }

    }

}
