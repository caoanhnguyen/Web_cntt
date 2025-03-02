package com.kma.api;

import com.kma.models.errorResponseDTO;
import com.kma.services.Impl.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    // API gửi thông báo tới một userId cụ thể
    @PostMapping("/send")
    public ResponseEntity<Object> sendNotificationByUserId(@RequestParam String userId,
                                                           @RequestParam String title,
                                                           @RequestParam String body,
                                                           @RequestParam String url) {
        try {
//            String result = notificationService.sendNotificationByUserId(userId, title, body, url);
//            return new ResponseEntity<>(result, HttpStatus.OK);
            return null;
        } catch (Exception e) {
            return buildErrorResponse(e, "Failed to send notification to userId: " + userId);
        }
    }

    // API gửi thông báo tới toàn bộ user
    @PostMapping("/send-to-all")
    public ResponseEntity<Object> sendNotificationToAllUsers(@RequestParam String title,
                                                             @RequestParam String body,
                                                             @RequestParam String url) {
        try {
            // Gửi thông báo đến tất cả user
//            String result = notificationService.sendNotificationToAllUsers(title, body, url);
//            return new ResponseEntity<>(result, HttpStatus.OK);
            return null;
        } catch (Exception e) {
            return buildErrorResponse(e, "Failed to send notification to all users");
        }
    }

    // Hàm tạo ResponseEntity cho lỗi
    private ResponseEntity<Object> buildErrorResponse(Exception e, String message) {
        errorResponseDTO errorDTO = new errorResponseDTO();
        errorDTO.setError(message);
        List<String> details = new ArrayList<>();
        details.add(e.getMessage());
        errorDTO.setDetails(details);

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}

