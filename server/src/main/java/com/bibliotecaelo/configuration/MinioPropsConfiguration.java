package com.bibliotecaelo.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "minio")
public class MinioPropsConfiguration {

    private String url;
    private String accessKey;
    private String secretKey;
    private String bucketName;

}