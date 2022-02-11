package com.zt.mypassword.shiro.exception.custom;

import com.zt.mypassword.enums.SystemStatusCode;
import org.apache.shiro.authc.AuthenticationException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/13 15:07
 * description: accessToken过期
 */
public class JwtAccessTokenErrorException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public JwtAccessTokenErrorException() {
        super(SystemStatusCode.JWT_ACCESS_TOKEN_ERROR.getName());
    }
}
