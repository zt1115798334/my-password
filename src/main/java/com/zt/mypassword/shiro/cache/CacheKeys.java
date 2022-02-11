package com.zt.mypassword.shiro.cache;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/13 14:04
 * description:
 */
public class CacheKeys {
    /**
     * 用户登录次数计数
     */
    private static final String PREFIX_SHIRO_LOGIN_COUNT = "shiro:login_count:";
    /**
     * 用户登录是否被锁定
     */
    private static final String PREFIX_SHIRO_IS_LOCK = "shiro:is_lock:";
    /**
     * shiro 缓存
     */
    private static final String PREFIX_SHIRO_CACHE = "shiro:cache:";
    /**
     * jwt验证token
     */
    private static final String PREFIX_JWT_ACCESS_TOKEN = "jwt:access_token:";
    /**
     * jwt刷新token
     */
    private static final String PREFIX_JWT_REFRESH_TOKEN = "jwt:refresh_token:";

    public static String getShiroLoginCountKey(String account) {
        return PREFIX_SHIRO_LOGIN_COUNT + account;
    }

    public static String getShiroIsLockKey(String account) {
        return PREFIX_SHIRO_IS_LOCK + ":" + account;
    }

    public static String getJwtAccessTokenKey(Long userId, Long ipLong) {
        return PREFIX_JWT_ACCESS_TOKEN + userId + ":" + ipLong;
    }

    public static String getJwtRefreshTokenKey(Long userId, Long ipLong) {
        return PREFIX_JWT_REFRESH_TOKEN + userId + ":" + ipLong;
    }

}
