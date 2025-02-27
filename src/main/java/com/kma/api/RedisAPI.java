package com.kma.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class RedisAPI {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/redis/ping")
    public ResponseEntity<String> pingRedis() {
        try {
            redisTemplate.opsForValue().set("ping", "pong", 10, TimeUnit.SECONDS);
            return ResponseEntity.ok("Redis is working!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Redis error: " + e.getMessage());
        }
    }
}
