package com.kma.services.Impl;

import com.kma.services.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FcmServiceImpl implements FcmService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    // Thêm token vào Redis (dùng Redis Set để quản lý nhiều token cho một user)
    @Override
    public void addToken(String userId, String fcmToken) {
        String redisKey = "fcm_tokens:" + userId;
        redisTemplate.opsForSet().add(redisKey, fcmToken);
    }

    // Xóa một token cụ thể khỏi Redis
    @Override
    public void removeToken(String userId, String fcmToken) {
        String redisKey = "fcm_tokens:" + userId;
        redisTemplate.opsForSet().remove(redisKey, fcmToken);
    }

    // Xóa toàn bộ token của một user
    @Override
    public void removeAllTokens(String userId) {
        String redisKey = "fcm_tokens:" + userId;
        redisTemplate.delete(redisKey);
    }

    // Lấy tất cả token của một user
    @Override
    public Set<String> getTokenByUserId(String userId) {
        String redisKey = "fcm_tokens:" + userId;
        return redisTemplate.opsForSet().members(redisKey);
    }

    // Lấy tất cả FCM token từ Redis
    @Override
    public List<String> getAllTokens() {
        List<String> tokens = new ArrayList<>();

        // Lấy tất cả các key có prefix "fcm_tokens:"
        Set<String> keys = redisTemplate.keys("fcm_tokens:*");

        if (keys != null && !keys.isEmpty()) {
            // Lấy từng token từ Redis và thêm vào danh sách
            for (String key : keys) {
                String token = redisTemplate.opsForValue().get(key);
                if (token != null) {
                    tokens.add(token);
                }
            }
        }

        return tokens;
    }
}

