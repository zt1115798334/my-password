package com.zt.mypassword.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/24 13:55
 * description:
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "custom.file")
public class FileProperties {

    private String profilesPicturePath;

}
