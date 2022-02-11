package com.zt.mypassword.shiro.exception.custom;

import com.zt.mypassword.enums.SystemStatusCode;
import org.apache.shiro.authc.AuthenticationException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/13 14:41
 * description: jwt未找到
 */
public class JwtNotFunException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public JwtNotFunException() {
        super(SystemStatusCode.JWT_NOT_FOUND.getName());
    }
}
