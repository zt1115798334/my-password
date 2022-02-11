package com.zt.mypassword.shiro.realm;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import com.zt.mypassword.cache.StringRedisService;
import com.zt.mypassword.mysql.entity.Permission;
import com.zt.mypassword.mysql.entity.User;
import com.zt.mypassword.mysql.service.PermissionService;
import com.zt.mypassword.mysql.service.UserService;
import com.zt.mypassword.service.InheritService;
import com.zt.mypassword.shiro.cache.CacheKeys;
import com.zt.mypassword.shiro.exception.custom.JwtAccessTokenErrorException;
import com.zt.mypassword.shiro.exception.custom.JwtAccessTokenExpireException;
import com.zt.mypassword.shiro.exception.custom.JwtNotFunException;
import com.zt.mypassword.shiro.exception.custom.UserNotFoundException;
import com.zt.mypassword.shiro.token.JwtToken;
import com.zt.mypassword.utils.JwtUtils;
import com.zt.mypassword.utils.NetworkUtil;
import com.zt.mypassword.enums.DeleteState;
import com.zt.mypassword.enums.SystemStatusCode;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/13 13:57
 * description:
 */
@Slf4j
public class JwtRealm extends AuthorizingRealm {

    @Setter
    private StringRedisService stringRedisService;

    @Setter
    private UserService userService;

    @Setter
    private InheritService inheritService;

    @Setter
    private PermissionService permissionService;

    @Override
    public Class<?> getAuthenticationTokenClass() {
        return JwtToken.class;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        Set<String> permissionSet = Sets.newHashSet();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (user != null) {
            if (Objects.equal(user.getDeleteState(), DeleteState.DELETE)) {
                permissionSet.add(SystemStatusCode.USER_DELETE.getName());
            } else {
                permissionSet.add(SystemStatusCode.USER_NORMAL.getName());
                List<Permission> permissionList = inheritService.findPermissionByUserId(user.getId(),user.getAccountType());
                Set<String> collect = permissionList.stream()
                        .map(Permission::getPermission)
                        .filter(StringUtils::isNotEmpty)
                        .collect(Collectors.toSet());
                permissionSet.addAll(collect);
            }

        } else {
            permissionSet.add(SystemStatusCode.USER_NOT_FOUND.getName());
        }
        info.setStringPermissions(permissionSet);

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        if (!(authenticationToken instanceof JwtToken)) {
            return null;
        }
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String token = (String) jwtToken.getCredentials();
        Long userId = jwtToken.getUserId();
        String ip = jwtToken.getIp();
        Long ipLong = NetworkUtil.ipToLong(ip);
        if (StringUtils.isNotBlank(token) && userId != null) {
            Optional<String> accessTokenOpt = stringRedisService.get(CacheKeys.getJwtAccessTokenKey(userId, ipLong));
            if (accessTokenOpt.isPresent()) {
                Optional<User> userOptional = userService.findByIdNotDel(userId);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    if (JwtUtils.verify(token, user.getPassword())) {
                        return new SimpleAuthenticationInfo(user, token, getName());
                    } else {
                        throw new JwtAccessTokenErrorException();
                    }
                } else {
                    throw new UserNotFoundException();
                }
            } else {
                throw new JwtAccessTokenExpireException();
            }
        } else {
            throw new JwtNotFunException();
        }

    }
}
