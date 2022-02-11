package com.zt.mypassword.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.zt.mypassword.mysql.entity.User;
import com.zt.mypassword.utils.DateUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/13 11:03
 * description:
 */
public class JwtUtils {
    public static final String TOKEN_CLAIM = "userId";
    public static final long ACCESS_EXPIRATION = 1;
    public static final TimeUnit ACCESS_TIMEUNIT = TimeUnit.HOURS;
    public static final long REFRESH_EXPIRATION = 6;
    public static final TimeUnit REFRESH_TIMEUNIT = TimeUnit.HOURS;

    private static Date generateExpirationDate(long expiration, TimeUnit timeUnit) {
        return DateUtils.localDateTimeToDate(DateUtils.currentDateTimeAddTime(expiration, timeUnit));
    }

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String secret) {
        try {
            //根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(TOKEN_CLAIM, getUserId(token))
                    .build();
            // 效验TOKEN
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    /**
     * 生成jwt
     *
     * @param user user
     * @return token
     */
    public static String generateAccessToken(User user) {
        return generateToken(user, ACCESS_EXPIRATION, ACCESS_TIMEUNIT);
    }

    /**
     * 生成jwt
     *
     * @param user user
     * @return token
     */
    public static String generateRefreshToken(User user) {
        return generateToken(user, REFRESH_EXPIRATION, REFRESH_TIMEUNIT);
    }

    /**
     * 生成jwt
     *
     * @param user user
     * @return token
     */
    public static String generateToken(User user, long expiration, TimeUnit timeUnit) {
        Date expirationDate = generateExpirationDate(expiration, timeUnit);
        JWTCreator.Builder builder = JWT.create();
        builder.withClaim(TOKEN_CLAIM, user.getId()).withClaim("account", user.getAccount());
        return builder.withExpiresAt(expirationDate).sign(Algorithm.HMAC256(user.getPassword()));
    }

    public static Long getUserId(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("userId").asLong();
    }
}
