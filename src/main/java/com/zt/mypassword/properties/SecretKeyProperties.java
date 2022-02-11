package com.zt.mypassword.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/12/8
 * description:
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom.secret-key")
public class SecretKeyProperties {

    private String publicKey;
    private String privateKey;
    private String prefix;
    private String suffix;
}
