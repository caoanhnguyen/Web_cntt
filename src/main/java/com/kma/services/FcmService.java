package com.kma.services;

import java.util.Map;
import java.util.Set;

public interface FcmService {
    void addToken(String userId, String fcmToken); // Thêm token mới

    void removeToken(String userId, String fcmToken); // Xóa một token cụ thể

    void removeAllTokens(String userId); // Xóa toàn bộ token của user

    Set<String> getTokenByUserId(String userId); // Lấy tất cả token của user

    Map<String, Set<String>> getAllUserTokens();

}
