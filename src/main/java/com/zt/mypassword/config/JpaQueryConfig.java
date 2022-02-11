package com.zt.mypassword.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/23 17:16
 * description:
 */
@Configuration
public class JpaQueryConfig {
    @Bean
    public JPAQueryFactory jpaQuery(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
