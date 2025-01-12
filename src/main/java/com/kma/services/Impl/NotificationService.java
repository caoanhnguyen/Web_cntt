package com.kma.services.Impl;

import com.google.firebase.messaging.*;
import com.kma.models.TokenRequest;
import com.kma.services.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class NotificationService {

    @Autowired
    FcmService fcmServ;

    // Gửi thông báo trực tiếp bằng FCM token
    @Async("notificationExecutor")
    public void sendNotification(TokenRequest tokenRequest, String title, String body, String url) {
        try {
            // Tạo thông báo
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            // Tạo message
            Message message = Message.builder()
                    .setNotification(notification)
                    .putData("url", "http://localhost:8084/"+url)  // URL hoặc hành động cần thực hiện
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
    public String sendNotificationByUserId(String userId, String title, String body, String url) {
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
                sendNotification(tokenRequest, title, body, url);
            }

            return "Notification sent to all devices for userId: " + userId;

        } catch (Exception e) {
            // Xử lý lỗi chung
            return "Error sending notification to userId: " + e.getMessage();
        }
    }

    // Gửi thông báo tới tất cả user
    public String sendNotificationToAllUsers(String title, String body, String url) {
        try {
            // Lấy danh sách tất cả userId và token từ Redis
            Map<String, Set<String>> userTokens = fcmServ.getAllUserTokens();

            if (userTokens.isEmpty()) {
                return "No users found to send notification.";
            }

            // Gửi thông báo tới từng token
            for (Map.Entry<String, Set<String>> entry : userTokens.entrySet()) {
                String userId = entry.getKey();
                TokenRequest tokenRequest;
                for (String token : entry.getValue()) {
                    tokenRequest = new TokenRequest(userId, token);
                    sendNotification(tokenRequest, title, body, url);
                }
            }

            return "Notification sent to all users successfully.";
        } catch (Exception e) {
            throw new RuntimeException("Error while sending notification to all users: " + e.getMessage());
        }
    }

}
