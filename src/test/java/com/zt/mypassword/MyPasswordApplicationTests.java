package com.zt.mypassword;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.querydsl.core.Tuple;
import com.zt.mypassword.mysql.entity.QUser;
import com.zt.mypassword.properties.AccountProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
class MyPasswordApplicationTests {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CriteriaBuilderFactory criteriaBuilderFactory;

    @Test
    void contextLoads() {
        QUser user = QUser.user;
        BlazeJPAQuery<Tuple> query = new BlazeJPAQuery<>(entityManager, criteriaBuilderFactory).from(user)
                .select(user.userName.as("userName"), user.account.substring(2))
                .where(user.account.length().gt(1));
        List<Tuple> fetch = query.fetch();
        for (Tuple tuple : fetch) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Autowired
    private AccountProperties accountProperties;

    @Test
    void edPassword() {
        RSA rsa = SecureUtil.rsa(null, accountProperties.getPublishKey());
        System.out.println(rsa.encryptBcd("15600663638devil9498", KeyType.PublicKey));
    }
}
