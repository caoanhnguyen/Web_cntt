//package com.kma.utilities;
//
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//public class passwordEncoderUtil {
//    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//    // Mã hóa mật khẩu
//    public static String encode(String rawPassword) {
//        return encoder.encode(rawPassword);
//    }
//
//    // Kiểm tra mật khẩu
//    public static boolean matches(String rawPassword, String encodedPassword) {
//        return encoder.matches(rawPassword, encodedPassword);
//    }
//
//}
