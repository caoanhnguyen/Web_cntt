package com.kma.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class changePasswordDTO {
    // Getters và Setters
    private String oldPassword;   // Mật khẩu cũ
    private String newPassword;   // Mật khẩu mới
    private String confirmPassword; // Xác nhận mật khẩu mới

}
