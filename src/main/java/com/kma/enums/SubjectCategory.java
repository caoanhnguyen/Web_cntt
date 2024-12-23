package com.kma.enums;

public enum SubjectCategory {
    GENERAL("Đại cương"),           // Nhóm môn học đại cương
    FOUNDATION("Chuyên đề cơ sở"),  // Nhóm môn học chuyên đề cơ sở
    SPECIALIZED("Chuyên ngành");    // Nhóm môn học chuyên ngành

    private final String displayName;

    SubjectCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

