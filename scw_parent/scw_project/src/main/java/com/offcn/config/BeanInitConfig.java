package com.offcn.config;

import com.offcn.util.UploadFileTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanInitConfig {

    @ConfigurationProperties(prefix="oss")
    @Bean
    public UploadFileTemplate createUploadTemplate(){
        return new UploadFileTemplate();
    }
}
