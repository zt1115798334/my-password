package com.zt.mypassword.shiro.utils;

import com.google.common.base.Objects;
import com.zt.mypassword.mysql.entity.User;
import com.zt.mypassword.utils.Digests;
import com.zt.mypassword.utils.Encodes;
import com.zt.mypassword.enums.DeleteState;
import com.zt.mypassword.enums.EnabledState;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/25 14:51
 * description:
 */
public class UserUtils {

    public static String checkUserState(User user) {
        String result = "";
        if (Objects.equal(user.getDeleteState(), DeleteState.DELETE)) {
            result = "账户已被删除";
        } else if (Objects.equal(user.getEnabledState(), EnabledState.OFF)){
            result = "账户已被禁用";
        }
        return result;
    }


    /**
     * 获取加密密码
     *
     * @param account  账户
     * @param password 未加密密码
     * @param salt     盐
     * @return String
     */
    public static String getEncryptPassword(String account, String password, String salt) {
        byte[] hashPassword = Digests.sha1((account + password).getBytes(), Encodes.decodeHex(salt), Digests.HASH_INTERATIONS);
        return Encodes.encodeHex(hashPassword);
    }

}
