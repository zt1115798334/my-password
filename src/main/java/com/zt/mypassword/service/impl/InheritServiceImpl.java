package com.zt.mypassword.service.impl;

import cn.hutool.core.net.NetUtil;
import com.zt.mypassword.cache.StringRedisService;
import com.zt.mypassword.enums.AccountType;
import com.zt.mypassword.enums.VerificationCodeType;
import com.zt.mypassword.exception.custom.OperationException;
import com.zt.mypassword.mysql.entity.Permission;
import com.zt.mypassword.mysql.entity.User;
import com.zt.mypassword.mysql.service.PermissionService;
import com.zt.mypassword.service.InheritService;
import com.zt.mypassword.service.VerificationCodeService;
import com.zt.mypassword.shiro.cache.CacheKeys;
import com.zt.mypassword.shiro.token.PasswordToken;
import com.zt.mypassword.utils.JwtUtils;
import com.zt.mypassword.utils.NetworkUtil;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/13 17:38
 * description:
 */
@Service
@AllArgsConstructor
public class InheritServiceImpl implements InheritService {

    private final StringRedisService stringRedisService;

    private final PermissionService permissionService;

    private final VerificationCodeService verificationCodeService;

    @Override
    public String login(PasswordToken token, String verificationCode, String ip) {
        Long ipLong = NetUtil.ipv4ToLong(ip);
//        if (!verificationCodeService.validateVerificationCode(token.getUsername(), verificationCode, VerificationCodeType.LOGIN)) {
//            throw new OperationException("验证码不正确");
//        }
        SecurityUtils.getSubject().login(token);
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Long userId = user.getId();
        String accessToken = JwtUtils.generateAccessToken(user);
        String refreshToken = JwtUtils.generateRefreshToken(user);

        String jwtAccessTokenKey = CacheKeys.getJwtAccessTokenKey(userId, ipLong);
        String jwtRefreshTokenKey = CacheKeys.getJwtRefreshTokenKey(userId, ipLong);
        //token 存储redis
        stringRedisService.setContainExpire(jwtAccessTokenKey, accessToken, Duration.of(JwtUtils.ACCESS_EXPIRATION, JwtUtils.ACCESS_TIMEUNIT) );
        stringRedisService.setContainExpire(jwtRefreshTokenKey, refreshToken, Duration.of( JwtUtils.REFRESH_EXPIRATION, JwtUtils.REFRESH_TIMEUNIT));
        return accessToken;
    }

    @Override
    public void sendVerificationCodeByLogin(String username, String ip) {
        verificationCodeService.sendVerificationCode(ip, username, VerificationCodeType.LOGIN);
    }

    @Override
    public void logout(Long userId, String ip) {
        Long ipLong = NetUtil.ipv4ToLong(ip);
        stringRedisService.delete(CacheKeys.getJwtAccessTokenKey(userId, ipLong));
        stringRedisService.delete(CacheKeys.getJwtRefreshTokenKey(userId, ipLong));
        SecurityUtils.getSubject().logout();
    }

    @Override
    public List<Permission> findPermissionByUserId(Long userId, AccountType accountType) {
        return permissionService.findAllPermissions();
    }
}
