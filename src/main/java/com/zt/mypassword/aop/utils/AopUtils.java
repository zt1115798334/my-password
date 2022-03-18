package com.zt.mypassword.aop.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.RSA;
import com.zt.mypassword.utils.MStringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created with IntelliJ IDEA.
 *
 * @author ZT
 * date: 2020/2/18 13:12
 * description:
 */
public class AopUtils {

    public static String parseKey(String key, JoinPoint joinPoint, boolean keyTransformMd5) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        Object[] parameterValues = joinPoint.getArgs();
        String[] parameterNames = methodSignature.getParameterNames();

        assert parameterNames != null;
        String return_data_key = MStringUtils.parseParams(parameterNames, parameterValues);
        if (keyTransformMd5) {
            return_data_key = SecureUtil.md5(return_data_key);
        }
        return key == null ? null : return_data_key;
    }
}
