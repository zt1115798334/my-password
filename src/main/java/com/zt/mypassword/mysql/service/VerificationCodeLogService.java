package com.zt.mypassword.mysql.service;


import com.zt.mypassword.mysql.entity.VerificationCodeLog;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/03/20 09:37
 * description:
 */
public interface VerificationCodeLogService {


    void saveVerificationCodeLog(VerificationCodeLog verificationCodeLog);
}
