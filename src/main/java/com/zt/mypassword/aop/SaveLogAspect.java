package com.zt.mypassword.aop;

import com.alibaba.fastjson.JSONObject;
import com.zt.mypassword.base.user.CurrentUser;
import com.zt.mypassword.mysql.entity.User;
import com.zt.mypassword.mysql.entity.UserLog;
import com.zt.mypassword.mysql.service.UserLogService;
import com.zt.mypassword.utils.MStringUtils;
import com.zt.mypassword.utils.NetworkUtil;
import com.zt.mypassword.base.user.CurrentUser;
import com.zt.mypassword.mysql.service.UserLogService;
import com.zt.mypassword.utils.MStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/26 9:49
 * description:
 */
@Aspect
@Slf4j
@Component
public class SaveLogAspect implements CurrentUser {
    private final ThreadLocal<String> className = new ThreadLocal<>();
    private final ThreadLocal<String> methodName = new ThreadLocal<>();
    private final ThreadLocal<String> paramVal = new ThreadLocal<>();
    private final ThreadLocal<String> ip = new ThreadLocal<>();

    private final UserLogService userLogService;

    public SaveLogAspect(UserLogService userLogService) {
        this.userLogService = userLogService;
    }

    @Pointcut("execution( * com.zt.mypassword.controller..*.*(..)) && @annotation(logs)")
    private void aopPointCut(SaveLog logs) {

    }

    @Before(value = "aopPointCut(logs)", argNames = "joinPoint,logs")
    private void doBefore(JoinPoint joinPoint, SaveLog logs) {
        Signature signature = joinPoint.getSignature();
        className.set(signature.getDeclaringTypeName());
        methodName.set(signature.getName());
        MethodSignature methodSignature = (MethodSignature) signature;

        Object[] parameterValues = joinPoint.getArgs();
        String[] parameterNames = methodSignature.getParameterNames();

        paramVal.set("[" + MStringUtils.parseParams(parameterNames, parameterValues) + "]");
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        ip.set(NetworkUtil.getLocalIp(request));
    }

    @AfterReturning(returning = "object", pointcut = "aopPointCut(logs)", argNames = "object,logs")
    private void doAfterReturning(Object object, SaveLog logs) {
        //返回值
        String response = JSONObject.toJSONString(object);
        String desc = logs.desc();
        Long userId = null;
        String username = null;
        if (logs.containUser()) {
            User currentUser = getCurrentUser();
            if (currentUser != null) {
                userId = currentUser.getId();
                username = currentUser.getUserName();
            }
        }
        UserLog userLog = UserLog.builder()
                .userId(userId)
                .name(username)
                .type(desc)
                .content(paramVal.get())
                .ip(ip.get())
                .time(LocalDateTime.now())
                .classify(className.get())
                .fun(methodName.get())
                .response(response).build();
        userLogService.saveUserLog(userLog);
        className.remove();
        paramVal.remove();
        methodName.remove();
        ip.remove();
    }
}
