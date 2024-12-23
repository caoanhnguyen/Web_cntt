package com.kma.models;

public class entityInfo {
    private Object entityId;
    private String avaFileCode;

    // Constructor
    public entityInfo(Object entityId, String avaFileCode) {
        this.entityId = entityId;
        this.avaFileCode = avaFileCode;
    }

    public entityInfo() {

    }

    // Getter và Setter
    public Object getEntityId() {
        return entityId;
    }

    public void setEntityId(Object entityId) {
        this.entityId = entityId;
    }

    public String getAvaFileCode() {
        return avaFileCode;
    }

    public void setAvaFileCode(String avaFileCode) {
        this.avaFileCode = avaFileCode;
    }
}
