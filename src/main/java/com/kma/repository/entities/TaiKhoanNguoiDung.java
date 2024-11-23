package com.kma.repository.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="tai_khoan_nguoi_dung")
public class TaiKhoanNguoiDung {

    @Id
    @Column(name="tenDangNhap", nullable = false)
    private String tenDangNhap;

    @Column(name="matKhau")
    private String matKhau;

    // Config relation to nhan_vien
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser")
    private NhanVien nhanVien;

    //Config relation to roles
    @OneToMany(mappedBy = "taiKhoanNguoiDung", cascade = CascadeType.REMOVE)
    private List<Role> rolesList;

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public List<Role> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<Role> rolesList) {
        this.rolesList = rolesList;
    }
}
