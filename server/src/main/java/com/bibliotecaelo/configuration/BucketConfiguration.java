package com.bibliotecaelo.configuration;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BucketConfiguration {

    @Autowired
    MinioPropsConfiguration props;

    @Bean
    public MinioClient bucketClient() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(props.getUrl())
                .credentials(props.getAccessKey(), props.getSecretKey())
                .build();

        try {
            boolean bucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                    .bucket(props.getBucketName())
                    .build());

            if(!bucketExists) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder()
                        .bucket(props.getBucketName())
                        .build()
                );
            }
        } catch (MinioException e) {
            throw new RuntimeException(e);
        }

        return minioClient;
    }

}