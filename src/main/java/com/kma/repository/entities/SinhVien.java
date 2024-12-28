package com.kma.repository.entities;

import com.kma.enums.GioiTinh;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sinh_vien")
public class SinhVien {

    @Id
    @Column(name = "maSinhVien", nullable = false)
    private String maSinhVien;

    @Column(name = "tenSinhVien")
    private String tenSinhVien;

    @Enumerated(EnumType.STRING)
    @Column(name = "gioiTinh")
    private GioiTinh gioiTinh;

    @Column(name = "ngaySinh")
    private Date ngaySinh;

    @Column(name = "dienThoai")
    private String dienThoai;

    @Column(name = "email")
    private String email;

    @Column(name = "cccd", unique = true)
    private String cccd;

    @Column(name = "diaChiHienTai")
    private String diaChiHienTai;

    @Column(name = "queQuan")
    private String queQuan;

    @Column(name = "khoa")
    private String khoa;

    @Column(name = "chucVu")
    private String chucVu;

    @Column(name = "avaFileCode", unique = true)
    private String avaFileCode;

    //Config relation to Lop
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idLop")
    private Lop lop;

    //Config relation to dang_ky_su_kien
    @OneToMany(mappedBy = "sinhVien")
    private List<DangKySuKien> dkskList = new ArrayList<>();

    // Quan hệ 1-1 với User
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userId", referencedColumnName = "userId") // Khoá ngoại trỏ về User
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMaSinhVien() {
        return maSinhVien;
    }

    public void setMaSinhVien(String maSinhVien) {
        this.maSinhVien = maSinhVien;
    }

    public String getTenSinhVien() {
        return tenSinhVien;
    }

    public void setTenSinhVien(String tenSinhVien) {
        this.tenSinhVien = tenSinhVien;
    }

    public GioiTinh getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(GioiTinh gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getDiaChiHienTai() {
        return diaChiHienTai;
    }

    public void setDiaChiHienTai(String diaChiHienTai) {
        this.diaChiHienTai = diaChiHienTai;
    }

    public String getQueQuan() {
        return queQuan;
    }

    public void setQueQuan(String queQuan) {
        this.queQuan = queQuan;
    }

    public String getKhoa() {
        return khoa;
    }

    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getAvaFileCode() {
        return avaFileCode;
    }

    public void setAvaFileCode(String avaFileCode) {
        this.avaFileCode = avaFileCode;
    }

    public Lop getLop() {
        return lop;
    }

    public void setLop(Lop lop) {
        this.lop = lop;
    }

    public List<DangKySuKien> getDkskList() {
        return dkskList;
    }

    public void setDkskList(List<DangKySuKien> dkskList) {
        this.dkskList = dkskList;
    }
}
