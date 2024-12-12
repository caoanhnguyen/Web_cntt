package com.kma.models;

public class TokenRequest {
    private String userId;
    private String fcmToken;

    public TokenRequest() {

    }

    public TokenRequest(String userId, String fcmToken) {
        this.userId = userId;
        this.fcmToken = fcmToken;
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
