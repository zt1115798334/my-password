package com.zt.mypassword.aop.cache;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/13 14:04
 * description:
 */
public class CacheKeys {
    /**
     * 接口缓存
     */
    private static final String PREFIX_INTER = "inter:";

    public static String getInterKey(String value) {
        return PREFIX_INTER + value;
    }

}
