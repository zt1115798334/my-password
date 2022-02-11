package com.zt.mypassword.shiro.filter;

import com.google.common.collect.Maps;
import com.zt.mypassword.cache.StringRedisService;
import com.zt.mypassword.mysql.entity.Permission;
import com.zt.mypassword.mysql.service.PermissionService;
import com.zt.mypassword.mysql.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.cache.CacheManager;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/13 13:05
 * description:
 */
@Slf4j
@Component
@AllArgsConstructor
public class ShiroFilterChainManager {

    private final UserService userService;

    private final StringRedisService stringRedisService;

    private final PermissionService permissionService;

    public Map<String, Filter> initFilters(CacheManager cacheManager) {
        Map<String, Filter> filters = Maps.newLinkedHashMap();
        JwtFilter jwtFilter = new JwtFilter();
        jwtFilter.setUserService(userService);
        jwtFilter.setStringRedisService(stringRedisService);
        filters.put("JwtFilter", jwtFilter);
        return filters;
    }

    /**
     * 初始化获取过滤链规则
     *
     * @return Map<String, String>
     */
    public Map<String, String> initGetFilterChain() {
        Map<String, String> filterChain = Maps.newLinkedHashMap();
        filterChain.put("/file/gallery/*", "anon");
        filterChain.put("/api/login/login", "anon");
        filterChain.put("/api/external/*", "anon");
        filterChain.put("/api/file/*", "anon");
        filterChain.put("/api/login/logout", "JwtFilter");
        List<Permission> permissionList = permissionService.findAllPermissions();
        for (Permission permission : permissionList) {
            if (StringUtils.isNotEmpty(permission.getPermission())) {
                filterChain.put(permission.getUrl(), "JwtFilter,perms[" + permission.getPermission() + "]");
            }
        }
        filterChain.put("/api/**", "JwtFilter");

        return filterChain;
    }
}
