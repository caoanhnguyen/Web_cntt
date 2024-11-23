package com.kma.repository.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "sinh_vien")
public class SinhVien {

    @Id
    @Column(name = "MaSinhVien", nullable = false)
    private String maSinhVien;

    @Column(name = "TenSinhVien", nullable = false)
    private String tenSinhVien;

    @Column(name = "NgaySinh")
    private Date ngaySinh;

    @Column(name = "DienThoai")
    private String dienThoai;

    @Column(name = "Email")
    private String email;

    @Column(name = "CCCD")
    private String cccd;

    @Column(name = "DiaChiHienTai")
    private String diaChiHienTai;

    @Column(name = "QueQuan")
    private String queQuan;

    @Column(name = "Khoa")
    private String khoa;

    @Column(name = "ChucVu")
    private String chucVu;

    @Column(name = "MatKhau")
    private String matKhau;

    @Column(name = "avaFileCode")
    private String avaFileCode;

    @Column(name = "MaLop")
    private String maLop;

    //Config relation to Lop
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idLop")
    private Lop lop;

    //Config relation to dang_ky_su_kien
    @OneToMany(mappedBy = "sinhVien", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DangKySuKien> dkskList;

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

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getAvaFileCode() {
        return avaFileCode;
    }

    public void setAvaFileCode(String avaFileCode) {
        this.avaFileCode = avaFileCode;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
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
