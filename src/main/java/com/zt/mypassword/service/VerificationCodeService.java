package com.zt.mypassword.service;

import com.zt.mypassword.enums.VerificationCodeType;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/2/28 14:29
 * description:
 */
public interface VerificationCodeService {

    /**
     * 发送验证码
     *
     * @param ip            ip
     * @param noticeContent 需要通知的内容
     * @param verificationCodeType      通知的类型
     */
    void sendVerificationCode(String ip, String noticeContent, VerificationCodeType verificationCodeType);

    /**
     * 检验验证码
     *
     * @param noticeContent 需要通知的内容
     * @param code          验证码
     * @param verificationCodeType      通知的类型
     * @return boolean
     */
    boolean validateVerificationCode(String noticeContent,  String verificationCode, VerificationCodeType verificationCodeType);
}
