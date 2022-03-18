package com.zt.mypassword;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import com.zt.mypassword.shiro.utils.UserUtils;
import org.junit.jupiter.api.Test;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2022/3/18
 * description:
 */
public class MyTest {
    @Test
    void name() {
        String salt = Base64.encode(RandomUtil.randomBytes(8));

        String devil9498 = UserUtils.getEncryptPassword("15600663638", "devil9498", salt);
        System.out.println(salt);
        System.out.println(devil9498);
    }
}
