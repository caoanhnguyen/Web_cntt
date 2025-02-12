package com.kma.utilities;

import java.text.Normalizer;

public class SlugUtils {

    public static String toSlug(String title) {
        // Loại bỏ dấu tiếng Việt và các ký tự đặc biệt
        String normalized = Normalizer.normalize(title, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Thay thế chữ "Đ" và "đ" thành "D" và "d"
        normalized = normalized.replaceAll("Đ", "D").replaceAll("đ", "d");

        // Thay thế khoảng trắng và các ký tự đặc biệt bằng dấu "-"
        String slug = normalized.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");

        // Loại bỏ dấu "-" ở đầu hoặc cuối (nếu có)
        return slug.replaceAll("^-|-$", "");
    }

}
