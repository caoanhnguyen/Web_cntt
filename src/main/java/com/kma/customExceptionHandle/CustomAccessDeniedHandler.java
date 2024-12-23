package com.kma.customExceptionHandle;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Đặt mã trạng thái HTTP
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // Ghi thông báo lỗi vào body của response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String message = "{\"error\": \"Access Denied\", \"message\": \"" + accessDeniedException.getMessage() + "\"}";
        response.getWriter().write(message);
    }
}
