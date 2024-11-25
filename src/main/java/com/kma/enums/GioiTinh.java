package com.kma.enums;

public enum GioiTinh {
    NAM("Nam"),
    NỮ("Nữ"),
    KHÁC("Khác");

    private final String displayName;

    GioiTinh(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    public static GioiTinh fromDisplayName(String displayName) {
        for (GioiTinh gender : values()) {
            if (gender.getDisplayName().equalsIgnoreCase(displayName)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy giá trị phù hợp cho: " + displayName);
    }
}
