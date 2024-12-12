package com.kma.models;

public class TokenRequest {
    private String userId;
    private String fcmToken;

    public TokenRequest(String userId, String token) {
        this.userId = userId;
        this.fcmToken = token;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
