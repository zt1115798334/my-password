package com.zt.mypassword.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author ZT
 * date: 2020/2/18 13:05
 * description:
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ThreadSleep {

    String key() default "";

    boolean keyTransformMd5() default true;

    long expired() default 1;

    TimeUnit timeUnit() default TimeUnit.HOURS;
}
