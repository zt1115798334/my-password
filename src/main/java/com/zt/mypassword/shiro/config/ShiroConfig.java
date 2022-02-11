package com.zt.mypassword.shiro.config;

import com.zt.mypassword.shiro.factory.StatelessWebSubjectFactory;
import com.zt.mypassword.shiro.filter.ShiroFilterChainManager;
import com.zt.mypassword.shiro.realm.AModularRealmAuthenticator;
import com.zt.mypassword.shiro.realm.RealmManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/13 11:38
 * description:
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class ShiroConfig {

    private final RedisProperties redisProperties;

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, ShiroFilterChainManager shiroFilterChainManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setUnauthorizedUrl("/api/error/401");
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilters(shiroFilterChainManager.initFilters(redisCacheManager()));
        shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainManager.initGetFilterChain());
        log.info("shiro拦截器工厂注入成功");
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(RealmManager realmManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(new AModularRealmAuthenticator());
        securityManager.setRealms(realmManager.initRealmList());
        DefaultSessionStorageEvaluator evaluator = (DefaultSessionStorageEvaluator) ((DefaultSubjectDAO) securityManager.getSubjectDAO()).getSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(Boolean.FALSE);
        StatelessWebSubjectFactory subjectFactory = new StatelessWebSubjectFactory();
        securityManager.setSubjectFactory(subjectFactory);
//        securityManager.setCacheManager(cacheManager());  //注释掉 缓存会导致权限不会刷新 -- 以后想到办法后再修改。
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    private RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(redisProperties.getHost());
        redisManager.setPassword(redisProperties.getPassword());
        redisManager.setTimeout((int) redisProperties.getTimeout().toMillis());
        return redisManager;
    }

    private RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }


}
