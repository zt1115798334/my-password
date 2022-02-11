package com.zt.mypassword.shiro.realm;

import com.google.common.collect.Lists;
import com.zt.mypassword.cache.StringRedisService;
import com.zt.mypassword.mysql.service.PermissionService;
import com.zt.mypassword.mysql.service.UserService;
import com.zt.mypassword.properties.AccountProperties;
import com.zt.mypassword.service.InheritService;
import com.zt.mypassword.shiro.token.JwtToken;
import com.zt.mypassword.shiro.token.PasswordToken;
import lombok.AllArgsConstructor;
import org.apache.shiro.realm.Realm;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/13 13:11
 * description:
 */
@AllArgsConstructor
@Component
public class RealmManager {

    private final UserService userService;

    private final AccountProperties accountProperties;

    private final StringRedisService stringRedisService;

    private final InheritService inheritService;

    private final PermissionService permissionService;


    public List<Realm> initRealmList() {
        List<Realm> realmList = Lists.newArrayList();
        // ----- password
        PasswordRealm passwordRealm = new PasswordRealm();
        passwordRealm.setUserService(userService);
        passwordRealm.setAccountProperties(accountProperties);
        passwordRealm.setStringRedisService(stringRedisService);
        passwordRealm.setAuthenticationTokenClass(PasswordToken.class);
        realmList.add(passwordRealm);
        // ----- jwt
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setUserService(userService);
        jwtRealm.setStringRedisService(stringRedisService);
        jwtRealm.setInheritService(inheritService);
        jwtRealm.setPermissionService(permissionService);
        jwtRealm.setAuthenticationTokenClass(JwtToken.class);
        realmList.add(jwtRealm);

        return Collections.unmodifiableList(realmList);
    }
}
