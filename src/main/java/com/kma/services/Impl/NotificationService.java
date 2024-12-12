package com.kma.services.Impl;

import com.google.firebase.messaging.*;
import com.kma.models.TokenRequest;
import com.kma.services.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class NotificationService {

    @Autowired
    FcmService fcmServ;
    RedisTemplate<String, String> redisTemplate;

    // Gửi thông báo trực tiếp bằng FCM token
    public void sendNotification(TokenRequest tokenRequest, String title, String body) {
        try {
            // Tạo thông báo
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            // Tạo message
            Message message = Message.builder()
                    .setNotification(notification)
                    .setToken(tokenRequest.getFcmToken())
                    .build();

            // Gửi thông báo
            FirebaseMessaging.getInstance().send(message);

        } catch (FirebaseMessagingException e) {
            if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                // Nếu token không hợp lệ, xóa khỏi Redis
                fcmServ.removeToken(tokenRequest.getUserId(), tokenRequest.getFcmToken());
            } else {
                return;
            }
        }
    }

    // Gửi thông báo tới tất cả token của một userId
    public String sendNotificationByUserId(String userId, String title, String body) {
        try {
            // Lấy tất cả token từ Redis
            Set<String> fcmTokens = fcmServ.getTokenByUserId(userId);

            if (fcmTokens == null || fcmTokens.isEmpty()) {
                return "No FCM tokens found for userId: " + userId;
            }
            TokenRequest tokenRequest;
            // Gửi thông báo tới từng token
            for (String token : fcmTokens) {
                tokenRequest = new TokenRequest(userId, token);
                sendNotification(tokenRequest, title, body);
            }

            return "Notification sent to all devices for userId: " + userId;

        } catch (Exception e) {
            // Xử lý lỗi chung
            return "Error sending notification to userId: " + e.getMessage();
        }
    }

    // Gửi thông báo tới tất cả user
    public String sendNotificationToAllUsers(String title, String body) {
        try {
            // Lấy danh sách tất cả userId từ Redis hoặc database
            List<String> listFCMToken = fcmServ.getAllTokens(); // Thêm hàm này trong FcmService

            if (listFCMToken == null || listFCMToken.isEmpty()) {
                return "No users found to send notification.";
            }

            // Gửi thông báo tới từng userId
            for (String token : listFCMToken) {
                sendNotification(new TokenRequest("", token), title, body);
            }

            return "Notification sent to all users successfully.";
        } catch (Exception e) {
            throw new RuntimeException("Error while sending notification to all users: " + e.getMessage());
        }
    }
}
