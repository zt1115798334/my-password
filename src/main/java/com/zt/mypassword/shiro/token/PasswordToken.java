package com.zt.mypassword.shiro.token;

import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 23:32
 * description:
 */
@Setter
@Getter
public class PasswordToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 1L;

    public PasswordToken(String username, String password) {
        super(username, password);
    }
}
