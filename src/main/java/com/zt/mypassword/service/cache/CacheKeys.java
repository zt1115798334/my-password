package com.zt.mypassword.service.cache;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/12 15:30
 * description:
 */
public class CacheKeys {

    private static final String PREFIX_SMS_VERIFICATION_CODE_PREFIX = "users:verification_code:";


    private static final String PREFIX_VERIFICATION_CODE_NOTICE_COUNT = "verification_code:notice_count_";

    private static final String PREFIX_VERIFICATION_CODE_IP_COUNT = "verification_code:ip_count_";

    private static final String PREFIX_VERIFICATION_CODE_NOTICE_IS_LOCK = "verification_code:notice_count_is_lock_";

    private static final String PREFIX_VERIFICATION_CODE_IP_IS_LOCK = "verification_code_ip:count_is_lock_";

    public static String getVerificationCodeKey(String verificationCodeType, String phone) {
        return PREFIX_SMS_VERIFICATION_CODE_PREFIX + verificationCodeType + ":" + phone;
    }

    public static String getVerificationCodeNoticeCountKey(String notice) {
        return PREFIX_VERIFICATION_CODE_NOTICE_COUNT + notice;
    }

    public static String getVerificationCodeIpCountKey(String ip) {
        return PREFIX_VERIFICATION_CODE_IP_COUNT + ip;
    }

    public static String getVerificationCodeNoticeIsLockKey(String notice) {
        return PREFIX_VERIFICATION_CODE_NOTICE_IS_LOCK + notice;
    }

    public static String getVerificationCodeIpIsLockKey(String ip) {
        return PREFIX_VERIFICATION_CODE_IP_IS_LOCK + ip;
    }

}
