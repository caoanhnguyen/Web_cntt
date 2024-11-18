package com.kma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;

@Configuration
public class minioConfig {

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("nrefuLpa622wvoqZHF00", "Wx2H2fBFd8RG2CTtoAUP3fPBk631sLRAQQatZ4tp")
                .build();
    }
}

