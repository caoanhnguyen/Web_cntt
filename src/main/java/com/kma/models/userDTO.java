package com.kma.models;

public class userDTO {
    private Object userId;
    private String name;
    private String avaFileCode;

    public userDTO(Object userId, String name, String avaFileCode) {
        this.userId = userId;
        this.name = name;
        this.avaFileCode = avaFileCode;
    }

    public userDTO() {
        
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvaFileCode() {
        return avaFileCode;
    }

    public void setAvaFileCode(String avaFileCode) {
        this.avaFileCode = avaFileCode;
    }
}
