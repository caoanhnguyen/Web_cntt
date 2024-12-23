package com.kma.models;

public class monHocResponseDTO {
    private Integer monHocID;
    private String tenMonHoc;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMonHocID() {
        return monHocID;
    }

    public void setMonHocID(Integer monHocID) {
        this.monHocID = monHocID;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }
}
