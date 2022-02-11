package com.zt.mypassword.shiro.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/19 10:39
 * description:
 */
@Setter
@Getter
@AllArgsConstructor
public class JwtToken implements AuthenticationToken {

    private Long userId;         //用户的标识
    private String ip;        //用户的IP
    private String token;           //json web token值

    @Override
    public Object getPrincipal() {
        return this.userId;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }
}
