package com.kma.enums;

public enum EventStatus {
    CHƯA("Chưa diễn ra"),
    ĐANG("Đang diễn ra"),
    ĐÃ("Đã kết thúc");

    private final String displayName;

    EventStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    public static EventStatus fromDisplayName(String displayName) {
        for (EventStatus eventSTT : values()) {
            if (eventSTT.getDisplayName().equalsIgnoreCase(displayName)) {
                return eventSTT;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy giá trị phù hợp cho: " + displayName);
    }
}
