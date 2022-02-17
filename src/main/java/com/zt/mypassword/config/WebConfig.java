package com.zt.mypassword.config;

import com.zt.mypassword.properties.FileProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/12/13
 * description:
 */
@AllArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final FileProperties fileProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(fileProperties.getProfilesPicturePath() + "/**").addResourceLocations("file:" + fileProperties.getProfilesPicturePath() + "/");
    }
}
