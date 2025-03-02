package com.kma.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.connection.RedisConnectionFactory;

@Configuration
public class RedisConfig {

    @Value("${SPRING_REDIS_HOST}")
    private String redisHost;

    @Value("${SPRING_REDIS_PORT}")
    private int redisPort;

    @Value("${SPRING_REDIS_PASSWORD}")
    private String redisPassword;

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        // Cấu hình kiểu serializer cho key và value
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(RedisSerializer.string());
        return template;
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {

        System.out.println("✅ Connecting to Redis at " + redisHost + ":" + redisPort);
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setPassword(redisPassword);
        return new LettuceConnectionFactory(config);
    }
}
