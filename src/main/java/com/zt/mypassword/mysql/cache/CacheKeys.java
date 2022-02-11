package com.zt.mypassword.mysql.cache;

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
    private static final String PREFIX_FIELD = "field:";

    public static String getFieldKey(Long fieldId) {
        return PREFIX_FIELD + fieldId;
    }

}
