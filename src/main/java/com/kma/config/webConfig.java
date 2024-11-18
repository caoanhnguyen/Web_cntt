package com.kma.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class webConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://172.20.10.18:8083", "http://localhost:8083", "http://127.0.0.1:5501") // Địa chỉ front-end
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*") // Cho phép tất cả các header
                .allowCredentials(true); // Nếu cần thiết
    }
}
