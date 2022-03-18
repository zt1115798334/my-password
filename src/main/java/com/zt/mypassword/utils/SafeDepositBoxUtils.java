package com.zt.mypassword.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2022/3/17
 * description:
 */
public class SafeDepositBoxUtils {

    /**
     * 解密
     * @param value 数据
     * @param rsaPrivateKey 私钥
     * @return 解密结果
     */
    public static String decrypt(String value,String rsaPrivateKey) {
       return SecureUtil.rsa(rsaPrivateKey, null).decryptStr(value, KeyType.PrivateKey);
    }
    /**
     * 加密
     * @param value 数据
     * @param rsaPublishKey 公钥
     * @return 加密结果
     */
    public static String encrypt(String value,String rsaPublishKey) {
        return SecureUtil.rsa(null, rsaPublishKey).encryptBase64(value, KeyType.PublicKey);
    }

    public static void main(String[] args) {
        String ddd = "dfasdfsasafsa";
        String public_key = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALZW6GKyiCSpyMbWoWCwQRnqas/JG7vbN9eTA4BhSTED9KwCC7Ysz45YPS8eEoNspXgpIewONZnwuTjDQRYaBeUCAwEAAQ==”";
        String private_key = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAtlboYrKIJKnIxtahYLBBGepqz8kbu9s315MDgGFJMQP0rAILtizPjlg9Lx4Sg2yleCkh7A41mfC5OMNBFhoF5QIDAQABAkBPsnrOMOSKyd8k8ckFfuRllG1tNqS1nmKxCeP86Ajar/aOCHAlD7BpkYxLomktBQ4ZL0xToThKVxBbkUrlDDEBAiEA9Tf7Atzhw1JrohAPxu0dhEPUX1lPhWsn8EiVdk+qlUECIQC+WzNZ+7YCpr1NvkVZSuokBHYHI3BSC2G57WcQqxQTpQIgKXYqqeRBA+6GpmNC16a16+wwF2MZb/ybRyRuqQ91T0ECIDLS8uEBndUTY9PQC4ANynoXXtQFGEYvl8YmprACnXepAiBXafLWdOM8IlXyJ1Khwlz+8CvbbR5ow9Tw+re3ctgw+w==";
        String encrypt = encrypt(ddd, public_key);
        System.out.println(encrypt);

        String decrypt = decrypt(encrypt, private_key);
        System.out.println(decrypt);


    }
}
