package com.zt.mypassword.service;

import com.zt.mypassword.mysql.entity.Permission;
import com.zt.mypassword.shiro.token.PasswordToken;
import com.zt.mypassword.enums.AccountType;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/13 17:38
 * description:
 */
public interface InheritService {

    String login(PasswordToken token, String verificationCode, String ip);

    void sendVerificationCodeByLogin(String username, String ip);

    void logout(Long userId, String ip);

    List<Permission> findPermissionByUserId(Long userId, AccountType accountType);

}
