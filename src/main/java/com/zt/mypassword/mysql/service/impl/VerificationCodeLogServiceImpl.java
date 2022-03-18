package com.zt.mypassword.mysql.service.impl;

import com.zt.mypassword.mysql.entity.VerificationCodeLog;
import com.zt.mypassword.mysql.repo.VerificationCodeLogRepository;
import com.zt.mypassword.mysql.service.VerificationCodeLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/03/20 09:37
 * description:
 */
@AllArgsConstructor
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class VerificationCodeLogServiceImpl implements VerificationCodeLogService {

    private final VerificationCodeLogRepository verificationCodeLogRepository;

    @Override
    public void saveVerificationCodeLog(VerificationCodeLog verificationCodeLog) {
        verificationCodeLog.setCreatedTime(LocalDateTime.now());
         verificationCodeLogRepository.save(verificationCodeLog);
    }
}
