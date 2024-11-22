package com.kma.models;

public class nhanVienResponseDTO {
    private Integer idUser;
    private String tenNhanVien;
    private String avaFileCode;

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public String getAvaFileCode() {
        return avaFileCode;
    }

    public void setAvaFileCode(String avaFileCode) {
        this.avaFileCode = avaFileCode;
    }
}
