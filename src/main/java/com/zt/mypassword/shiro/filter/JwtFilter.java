package com.zt.mypassword.shiro.filter;

import cn.hutool.core.net.NetUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zt.mypassword.cache.StringRedisService;
import com.zt.mypassword.mysql.entity.User;
import com.zt.mypassword.mysql.service.UserService;
import com.zt.mypassword.shiro.cache.CacheKeys;
import com.zt.mypassword.shiro.exception.custom.JwtAccessTokenErrorException;
import com.zt.mypassword.shiro.exception.custom.JwtAccessTokenExpireException;
import com.zt.mypassword.shiro.exception.custom.JwtNotFunException;
import com.zt.mypassword.shiro.exception.custom.UserNotFoundException;
import com.zt.mypassword.shiro.token.JwtToken;
import com.zt.mypassword.utils.JwtUtils;
import com.zt.mypassword.utils.NetworkUtil;
import com.zt.mypassword.utils.RequestResponseUtil;
import com.zt.mypassword.base.controller.ResultMessage;
import com.zt.mypassword.enums.SystemStatusCode;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/13 13:06
 * description:
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {
    @Setter
    private UserService userService;

    @Setter
    private StringRedisService stringRedisService;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        return (null != subject && subject.isAuthenticated());
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        try {
            AuthenticationToken token = createToken(request);
            this.getSubject(request, response).login(token);
            return true;
        } catch (AuthenticationException e) {
            if (e instanceof JwtAccessTokenExpireException) { //token过期
//                判断RefreshToken未过期就进行AccessToken刷新
                if (!this.refreshToken(request, response)) {
                    //jwt 已过期,通知客户端重新登录
                    ResultMessage message = new ResultMessage().error(SystemStatusCode.JWT_EXPIRE.getCode(), SystemStatusCode.JWT_EXPIRE.getName());
                    RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
                }
            }
            if (e instanceof JwtAccessTokenErrorException) { //token过期
                ResultMessage message = new ResultMessage().error(SystemStatusCode.JWT_ERROR.getCode(), SystemStatusCode.JWT_ERROR.getName());
                RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
            }
            if (e instanceof JwtNotFunException) { //没有发现token
                ResultMessage message = new ResultMessage().error(SystemStatusCode.JWT_NOT_FOUND.getCode(), SystemStatusCode.JWT_NOT_FOUND.getName());
                RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
            }

            if (e instanceof UserNotFoundException) { //没有发现账户
                ResultMessage message = new ResultMessage().error(SystemStatusCode.USER_NOT_FOUND.getCode(), SystemStatusCode.USER_DELETE.getName());
                RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
            }
        }
        return false;
    }

    private AuthenticationToken createToken(ServletRequest request) {
        Map<String, String> requestHeaders = RequestResponseUtil.getRequestHeaders(request);
        String ip = NetworkUtil.getLocalIp(RequestResponseUtil.getRequest(request));
        String token = Optional.ofNullable(requestHeaders.get("authorization")).orElseThrow(JwtNotFunException::new).substring(7);

        Long userId = JwtUtils.getUserId(token);
        return new JwtToken(userId, ip, token);
    }

    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        Map<String, String> requestHeaders = RequestResponseUtil.getRequestHeaders(request);
        String token = Optional.ofNullable(requestHeaders.get("authorization")).orElse("Bearer ").substring(7);
        Long userId = JwtUtils.getUserId(token);
        String ip = NetworkUtil.getLocalIp(RequestResponseUtil.getRequest(request));
        Long ipLong = NetUtil.ipv4ToLong(ip);

        String jwtAccessTokenKey = CacheKeys.getJwtAccessTokenKey(userId, ipLong);
        String jwtRefreshTokenKey = CacheKeys.getJwtRefreshTokenKey(userId, ipLong);
        Optional<String> refreshTokenOption = stringRedisService.get(jwtRefreshTokenKey);
        if (refreshTokenOption.isPresent()) {

            Optional<User> userOptional = userService.findByIdNotDel(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String accessToken = JwtUtils.generateAccessToken(user);
                String refreshToken = JwtUtils.generateRefreshToken(user);
                //token 存储redis
                stringRedisService.setContainExpire(jwtAccessTokenKey, accessToken, Duration.of( JwtUtils.ACCESS_EXPIRATION, JwtUtils.ACCESS_TIMEUNIT));
                stringRedisService.setContainExpire(jwtRefreshTokenKey, refreshToken,  Duration.of(JwtUtils.REFRESH_EXPIRATION, JwtUtils.REFRESH_TIMEUNIT));
                //发送新的token
                JSONObject tokenJSON = new JSONObject();
                tokenJSON.put("accessToken", accessToken);
                ResultMessage message = new ResultMessage()
                        .correctness(SystemStatusCode.JWT_NEW.getCode(), SystemStatusCode.JWT_NEW.getName())
                        .setData(tokenJSON);
                RequestResponseUtil.responseWrite(JSON.toJSONString(message), response);
                return true;
            }
        }

        return false;
    }
}
