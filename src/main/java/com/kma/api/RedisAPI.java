package com.kma.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/redis")
public class RedisAPI {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/ping")
    public ResponseEntity<String> pingRedis() {
        try {
            redisTemplate.opsForValue().set("ping", "pong", 10, TimeUnit.SECONDS);
            return ResponseEntity.ok("Redis is working!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Redis error: " + e.getMessage());
        }
    }

    // Ghi dữ liệu vào Redis
    @PostMapping("/set")
    public String setValue(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForValue().set(key, value);
        return "✅ Key '" + key + "' saved with value '" + value + "'";
    }

    // Đọc dữ liệu từ Redis
    @GetMapping("/get")
    public String getValue(@RequestParam String key) {
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? "🔍 Key '" + key + "' has value '" + value + "'" : "❌ Key '" + key + "' not found";
    }}
