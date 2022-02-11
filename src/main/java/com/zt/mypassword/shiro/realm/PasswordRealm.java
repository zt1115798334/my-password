package com.zt.mypassword.shiro.realm;

import com.google.common.base.Objects;
import com.zt.mypassword.cache.StringRedisService;
import com.zt.mypassword.mysql.entity.User;
import com.zt.mypassword.mysql.service.UserService;
import com.zt.mypassword.properties.AccountProperties;
import com.zt.mypassword.shiro.cache.CacheKeys;
import com.zt.mypassword.shiro.token.PasswordToken;
import com.zt.mypassword.shiro.utils.UserUtils;
import com.zt.mypassword.utils.RSAUtils;
import com.zt.mypassword.utils.DateUtils;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
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
        if (!(authenticationToken instanceof PasswordToken)) {
            return null;
        }
        PasswordToken token = (PasswordToken) authenticationToken;
        String name = token.getUsername();
        String shiroLoginCountKey = CacheKeys.getShiroLoginCountKey(name);
        String shiroIsLockKey = CacheKeys.getShiroIsLockKey(name);

        String username = String.valueOf(token.getUsername());

        String decode = new String(RSAUtils.decryptByPrivateKey(Base64.decodeBase64(String.valueOf(token.getPassword())), Base64.decodeBase64(accountProperties.getPrivateKey())));
        String password = decode.substring(username.length());

        Optional<String> shiroIsLock = stringRedisService.get(shiroIsLockKey);
        if (shiroIsLock.isPresent()) {
            if ("LOCK".equals(shiroIsLock.get())) {
                Long expireTime = stringRedisService.expireTime(shiroIsLockKey);
                throw new DisabledAccountException("由于多次输入错误密码，帐号已经禁止登录，请" + DateUtils.printHourMinuteSecond(expireTime) + "后重新尝试。");
            }
        }

        //密码进行加密处理  明文为  password+name
        Optional<User> userOptional = userService.findByAccountNotDel(name);
        if (!userOptional.isPresent()) {
            //登录错误开始计数
            String msg = increment(shiroLoginCountKey, shiroIsLockKey);
            throw new AccountException(msg);
        }
        User sysUser = userOptional.get();
        String userState = UserUtils.checkUserState(sysUser);
        if (StringUtils.isNotBlank(userState)) {
            //登录错误开始计数
            throw new AccountException(userState);
        }
        String pawDes = UserUtils.getEncryptPassword(username, password, sysUser.getSalt());
        // 从数据库获取对应用户名密码的用户
        String sysUserPassword = sysUser.getPassword();
        if (!Objects.equal(sysUserPassword, pawDes)) {
            //登录错误开始计数
            String msg = increment(shiroLoginCountKey, shiroIsLockKey);
            throw new AccountException(msg);
        } else {
            //登录成功
            //更新登录时间 last login time
            userService.updateLastLoginTime(sysUser.getId());
            //清空登录计数
            stringRedisService.setNotContainExpire(shiroLoginCountKey, "0");
            stringRedisService.delete(shiroIsLockKey);
        }
        log.info("身份认证成功，登录用户：" + name);
        return new SimpleAuthenticationInfo(sysUser, token.getPassword(), getName());
    }

    private String increment(String shiroLoginCountKey, String shiroIsLockKey) {
        //访问一次，计数一次
        stringRedisService.increment(shiroLoginCountKey, 1);
        //计数大于5时，设置用户被锁定10分钟
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
            return "帐号或密码错误！你还可以输入" + (count + 1) + "次";
        }).orElse("帐号或密码错误！");
    }


    public static void main(String[] args) {
        String pub = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKX/3pxfQQeqa3cnySBK0Q6Z2ZbFa0g160hNX57P5Az5IxZpcPE3qszw4rVUmYQzfuDDorrIk/uQ0+N2EsUCNXUCAwEAAQ==";
        String privateStr = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEApf/enF9BB6prdyfJIErRDpnZlsVrSDXrSE1fns/kDPkjFmlw8TeqzPDitVSZhDN+4MOiusiT+5DT43YSxQI1dQIDAQABAkAk3Im5XamaIyzT++C8rAB6Zo2b3mDxWw/RMWVPZci1XG3jZ9D/f2vFJhk8cHPHCkxIfGX5dceUw3C6UgPOGvuhAiEA1WkOCuq7n/RBAWpQRW7oJk79bwkp1KJ2UQYPRkeN9tkCIQDHIKKd7HFTJwoCSLfN6K66je1r5i+bioQVPiNn68Cp/QIhAI2RskoVqz+R1amtr/BPgVsgXUjSSFiRKXHYVjul7gwJAiBHFI031kj1p7/Y5OBHrkGA8lzH4VkpKp7PfmjhFbSmeQIhAICzeZ5qwy3FJ5oZvTjXZPQkknKrWoGwIguBiVtSGnN+";
        try {
            byte[] bytes = RSAUtils.encryptByPublicKey("xiaoming1234455".getBytes(), Base64.decodeBase64(pub));
            String st = Base64.encodeBase64String(bytes);
            System.out.println("st = " + st);
            String dedd = "NajjxkHrOFtHwGq2l07ICAXfyrRxDIt5LLu50GXmAdE+AFIbj3Ftlf90js03KPiE8kxQOAyfMwWyxmAKeb8sgQ==";
            byte[] decode1 = RSAUtils.decryptByPrivateKey(Base64.decodeBase64(dedd), Base64.decodeBase64(privateStr));
            System.out.println("解密后的数据：" + new String(decode1) + "\n\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
