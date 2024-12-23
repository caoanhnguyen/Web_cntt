package com.kma.api;

import com.kma.models.TokenRequest;
import com.kma.services.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class FcmController {

    @Autowired
    FcmService fcmService;

    // API lưu token
    @PostMapping("/store-fcm-token")
    public ResponseEntity<Map<String, String>> storeFcmToken(@RequestBody TokenRequest tokenRequest) {
        try {
            if (tokenRequest.getFcmToken() == null || tokenRequest.getFcmToken().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid FCM Token!"));
            }

            fcmService.addToken(tokenRequest.getUserId(), tokenRequest.getFcmToken());
            return ResponseEntity.ok(Map.of("message", "FCM Token stored successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error storing FCM Token: " + e.getMessage()));
        }
    }

    // API xóa một token cụ thể
    @PostMapping("/remove-fcm-token")
    public ResponseEntity<Map<String, String>> removeFcmToken(@RequestBody TokenRequest tokenRequest) {
        try {
            fcmService.removeToken(tokenRequest.getUserId(), tokenRequest.getFcmToken());
            return ResponseEntity.ok(Map.of("message", "FCM Token removed successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error removing FCM Token: " + e.getMessage()));
        }
    }

    // API xóa tất cả token của một user
    @PostMapping("/remove-all-fcm-tokens")
    public ResponseEntity<Map<String, String>> removeAllFcmTokens(@RequestParam String userId) {
        try {
            fcmService.removeAllTokens(userId);
            return ResponseEntity.ok(Map.of("message", "All FCM Tokens removed successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Error removing all FCM Tokens: " + e.getMessage()));
        }
    }

    // API lấy tất cả token của một user
    @GetMapping("/get-fcm-tokens")
    public ResponseEntity<Set<String>> getFcmTokens(@RequestParam String userId) {
        Set<String> tokens = fcmService.getTokenByUserId(userId);
        if (tokens == null || tokens.isEmpty()) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(tokens);
    }
}

