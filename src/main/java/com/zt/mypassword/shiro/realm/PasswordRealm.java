package com.zt.mypassword.shiro.realm;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import com.google.common.base.Objects;
import com.zt.mypassword.cache.StringRedisService;
import com.zt.mypassword.mysql.entity.User;
import com.zt.mypassword.mysql.service.UserService;
import com.zt.mypassword.properties.AccountProperties;
import com.zt.mypassword.shiro.cache.CacheKeys;
import com.zt.mypassword.shiro.token.PasswordToken;
import com.zt.mypassword.shiro.utils.UserUtils;
import com.zt.mypassword.utils.DateUtils;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/18 23:37
 * description:
 */
@Slf4j
public class PasswordRealm extends AuthorizingRealm {

    @Setter
    private UserService userService;

    @Setter
    private AccountProperties accountProperties;

    @Setter
    private StringRedisService stringRedisService;

    @Override
    public Class<?> getAuthenticationTokenClass() {
        return PasswordToken.class;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @SneakyThrows
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (!(authenticationToken instanceof PasswordToken token)) {
            return null;
        }
        String name = token.getUsername();
        String shiroLoginCountKey = CacheKeys.getShiroLoginCountKey(name);
        String shiroIsLockKey = CacheKeys.getShiroIsLockKey(name);

        String username = String.valueOf(token.getUsername());

        String decode = SecureUtil.rsa(accountProperties.getPrivateKey(), null).decryptStr(String.valueOf(token.getPassword()), KeyType.PrivateKey);
        String password = decode.substring(username.length());

        Optional<String> shiroIsLock = stringRedisService.get(shiroIsLockKey);
        if (shiroIsLock.isPresent()) {
            if ("LOCK".equals(shiroIsLock.get())) {
                Long expireTime = stringRedisService.expireTime(shiroIsLockKey);
                throw new DisabledAccountException("???????????????????????????????????????????????????????????????" + DateUtils.printHourMinuteSecond(expireTime) + "??????????????????");
            }
        }

        //????????????????????????  ?????????  password+name
        Optional<User> userOptional = userService.findByAccountNotDel(name);
        if (userOptional.isEmpty()) {
            //????????????????????????
            String msg = increment(shiroLoginCountKey, shiroIsLockKey);
            throw new AccountException(msg);
        }
        User sysUser = userOptional.get();
        String userState = UserUtils.checkUserState(sysUser);
        if (StringUtils.isNotBlank(userState)) {
            //????????????????????????
            throw new AccountException(userState);
        }
        String pawDes = UserUtils.getEncryptPassword(username, password, sysUser.getSalt());
        // ????????????????????????????????????????????????
        String sysUserPassword = sysUser.getPassword();
        if (!Objects.equal(sysUserPassword, pawDes)) {
            //????????????????????????
            String msg = increment(shiroLoginCountKey, shiroIsLockKey);
            throw new AccountException(msg);
        } else {
            //????????????
            //?????????????????? last login time
            userService.updateLastLoginTime(sysUser.getId());
            //??????????????????
            stringRedisService.setNotContainExpire(shiroLoginCountKey, "0");
            stringRedisService.delete(shiroIsLockKey);
        }
        log.info("????????????????????????????????????" + name);
        return new SimpleAuthenticationInfo(sysUser, token.getPassword(), getName());
    }

    private String increment(String shiroLoginCountKey, String shiroIsLockKey) {
        //???????????????????????????
        stringRedisService.increment(shiroLoginCountKey, 1);
        //????????????5???????????????????????????10??????
        return stringRedisService.get(shiroLoginCountKey).map(shiroLoginCount -> {
            Integer firstErrorAccountErrorCount = accountProperties.getFirstErrorAccountErrorCount();
            Integer secondErrorAccountErrorCount = accountProperties.getSecondErrorAccountErrorCount();
            Integer thirdErrorAccountErrorCount = accountProperties.getThirdErrorAccountErrorCount();
            int parseInt = Integer.parseInt(shiroLoginCount);
            int count = 0;
            if (parseInt <= firstErrorAccountErrorCount) {
                count = firstErrorAccountErrorCount - parseInt;
            } else if (parseInt <= secondErrorAccountErrorCount) {
                count = secondErrorAccountErrorCount - parseInt;
            } else if (parseInt <= thirdErrorAccountErrorCount) {
                count = thirdErrorAccountErrorCount - parseInt;
            }
            if (parseInt == firstErrorAccountErrorCount) {
                stringRedisService.expire(shiroIsLockKey, "LOCK", accountProperties.getFirstErrorAccountLockTime(), TimeUnit.MINUTES);
            } else if (parseInt == secondErrorAccountErrorCount) {
                stringRedisService.expire(shiroIsLockKey, "LOCK", accountProperties.getSecondErrorAccountLockTime(), TimeUnit.MINUTES);
            } else if (parseInt >= thirdErrorAccountErrorCount) {
                stringRedisService.expire(shiroIsLockKey, "LOCK", accountProperties.getThirdErrorAccountLockTime(), TimeUnit.HOURS);
            }
            return "??????????????????????????????????????????" + (count + 1) + "???";
        }).orElse("????????????????????????");
    }

}
