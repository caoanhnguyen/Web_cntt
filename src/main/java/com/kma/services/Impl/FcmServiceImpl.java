package com.kma.services.Impl;

import com.kma.services.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FcmServiceImpl implements FcmService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    // Thêm token vào Redis (dùng Redis Set để quản lý nhiều token cho một user)
    @Override
    public void addToken(String userId, String fcmToken) {
        String redisKey = "fcm_token:" + userId;
        redisTemplate.opsForSet().add(redisKey, fcmToken);
    }

    // Xóa một token cụ thể khỏi Redis
    @Override
    public void removeToken(String userId, String fcmToken) {
        String redisKey = "fcm_token:" + userId;
        redisTemplate.opsForSet().remove(redisKey, fcmToken);
    }

    // Xóa toàn bộ token của một user
    @Override
    public void removeAllTokens(String userId) {
        String redisKey = "fcm_token:" + userId;
        redisTemplate.delete(redisKey);
    }

    // Lấy tất cả token của một user
    @Override
    public Set<String> getTokenByUserId(String userId) {
        String redisKey = "fcm_token:" + userId;
        return redisTemplate.opsForSet().members(redisKey);
    }

    @Override
    // Lấy tất cả FCM token từ Redis
    public Map<String, Set<String>> getAllUserTokens() {
        Map<String, Set<String>> userTokens = new HashMap<>();
        Cursor<byte[]> cursor = null;

        try {
            // Tạo cursor để scan key từ Redis
            cursor = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection()
                    .scan(ScanOptions.scanOptions().match("fcm_token:*").count(50).build());

            // Lặp qua từng key được trả về
            while (cursor.hasNext()) {
                String key = new String(cursor.next());
                try {
                    // Tách userId từ key
                    String userId = key.split(":")[1]; // Phần sau "fcm_token:"

                    // Lấy tất cả token từ Redis Set
                    Set<String> tokens = redisTemplate.opsForSet().members(key);
                    if (tokens != null) {
                        userTokens.put(userId, tokens);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing key: " + key + ". Error: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.err.println("Error scanning Redis keys: " + e.getMessage());
        } finally {
            // Đảm bảo cursor được đóng
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                    System.err.println("Error closing Redis cursor: " + e.getMessage());
                }
            }
        }

        return userTokens;
    }


}

