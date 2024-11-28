package com.kma.repository.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="phong_ban")
public class PhongBan {
    @Id
    @Column(name = "maPhongBan", nullable = false)
    private String maPhongBan;

    @Column(name="tenPhongBan")
    private String tenPhongBan;

    @Column(name="ghiChu", columnDefinition = "LONGTEXT")
    private String ghiChu;

    //Config relation to nhan_vien
    @OneToMany(mappedBy = "phongBan")
    private List<NhanVien> nvList;

    public String getMaPhongBan() {
        return maPhongBan;
    }

    public void setMaPhongBan(String maPhongBan) {
        this.maPhongBan = maPhongBan;
    }

    public String getTenPhongBan() {
        return tenPhongBan;
    }

    public void setTenPhongBan(String tenPhongBan) {
        this.tenPhongBan = tenPhongBan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public List<NhanVien> getNvList() {
        return nvList;
    }

    public void setNvList(List<NhanVien> nvList) {
        this.nvList = nvList;
    }
}
