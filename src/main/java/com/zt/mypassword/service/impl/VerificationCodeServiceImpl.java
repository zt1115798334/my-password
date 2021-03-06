package com.zt.mypassword.service.impl;

import cn.hutool.core.net.NetUtil;
import com.zt.mypassword.cache.StringRedisService;
import com.zt.mypassword.enums.VerificationCodeType;
import com.zt.mypassword.exception.custom.OperationException;
import com.zt.mypassword.mysql.entity.VerificationCodeLog;
import com.zt.mypassword.mysql.service.VerificationCodeLogService;
import com.zt.mypassword.properties.VerificationCodeProperties;
import com.zt.mypassword.service.VerificationCodeService;
import com.zt.mypassword.service.cache.CacheKeys;
import com.zt.mypassword.utils.MStringUtils;
import com.zt.mypassword.utils.NetworkUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/2/28 14:30
 * description:
 */
@AllArgsConstructor
@Component
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeProperties verificationCodeProperties;

    private final StringRedisService stringRedisService;

    private final VerificationCodeLogService verificationCodeLogService;

    @Override
    public void sendVerificationCode(String ip, String noticeContent, VerificationCodeType verificationCodeType) {
        Long ipLong = NetUtil.ipv4ToLong(ip);
        String ipCountKey = CacheKeys.getVerificationCodeIpCountKey(String.valueOf(ipLong));
        String noticeContentCountKey = CacheKeys.getVerificationCodeNoticeCountKey(noticeContent);

        String ipIsLockKey = CacheKeys.getVerificationCodeIpIsLockKey(String.valueOf(ipLong));
        String noticeContentIsLockKey = CacheKeys.getVerificationCodeNoticeIsLockKey(noticeContent);

        Integer requestingPartyMaxCount = verificationCodeProperties.getCodeRequestingPartyMaxCount();
        Integer requestingPartyLockTime = verificationCodeProperties.getCodeRequestingPartyLockTime();

        //??????ip
        contentLock(ipCountKey, ipIsLockKey, requestingPartyMaxCount, requestingPartyLockTime);
        //???????????????????????????
        contentLock(noticeContentCountKey, noticeContentIsLockKey, requestingPartyMaxCount, requestingPartyLockTime);

        Optional<String> ipIsLockOptional = stringRedisService.get(ipIsLockKey);
        Optional<String> noticeContentIsLockOptional = stringRedisService.get(noticeContentIsLockKey);
        if (ipIsLockOptional.isPresent()) {
            if (Objects.equals("LOCK", ipIsLockOptional.get())) {
                throw new OperationException("??????????????????????????????24???????????????????????????");
            }
        }
        if (noticeContentIsLockOptional.isPresent()) {
            if (Objects.equals("LOCK", noticeContentIsLockOptional.get())) {
                throw new OperationException("??????????????????????????????24???????????????????????????");
            }
        }
        //??????
        stringRedisService.increment(ipCountKey, 1);
        stringRedisService.increment(noticeContentCountKey, 1);

        String codeKey;
        String code = null;
        codeKey = CacheKeys.getVerificationCodeKey(verificationCodeType.name(), noticeContent);
        code = RandomStringUtils.randomNumeric(verificationCodeProperties.getCodeLen());
        stringRedisService.setContainExpire(codeKey, code, Duration.of(verificationCodeProperties.getCodeExpires(), ChronoUnit.MINUTES));
        String smsCodeText = MStringUtils.splicingSmsCodeText(code);

        // TODO: 2021/11/29 ???????????????

        VerificationCodeLog verificationCodeLog = VerificationCodeLog.builder().noticeContent(noticeContent).verificationCode(code).verificationCodeType(verificationCodeType).ip(ip).build();
        verificationCodeLogService.saveVerificationCodeLog(verificationCodeLog);
    }

    @Override
    public boolean validateVerificationCode(String noticeContent, String verificationCode, VerificationCodeType verificationCodeType) {
        String codeKey = CacheKeys.getVerificationCodeKey(verificationCodeType.name(), noticeContent);
        Optional<String> stringOptional = stringRedisService.get(codeKey);
        if (stringOptional.isPresent()) {
            String redisCode = stringOptional.get();
            return Objects.equals(redisCode, verificationCode);
        }
        return false;
    }


    private void contentLock(String countKey, String isLockKey, Integer codeMaxCount, Integer codeMaxExpires) {
        stringRedisService.get(countKey).ifPresent(count -> {
            if (Integer.parseInt(count) >= codeMaxCount) {
                stringRedisService.expire(isLockKey, "LOCK", codeMaxExpires, TimeUnit.HOURS);
                stringRedisService.setNotContainExpire(countKey, "0");
            }
        });
    }
}
