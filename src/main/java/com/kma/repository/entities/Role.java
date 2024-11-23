package com.kma.repository.entities;

import jakarta.persistence.*;

@Entity
@Table(name="role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Column(name="quyen")
    private String quyen;

    @Column(name="isLock")
    private Integer isLock;

    // Config relation to tai_khoan_nguoi_dung
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenDangNhap")
    private TaiKhoanNguoiDung taiKhoanNguoiDung;

    // Config relation to phong_ban
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "maPhongBan")
    private PhongBan phongBan;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getQuyen() {
        return quyen;
    }

    public void setQuyen(String quyen) {
        this.quyen = quyen;
    }

    public Integer getIsLock() {
        return isLock;
    }

    public void setIsLock(Integer isLock) {
        this.isLock = isLock;
    }

    public TaiKhoanNguoiDung getTaiKhoanNguoiDung() {
        return taiKhoanNguoiDung;
    }

    public void setTaiKhoanNguoiDung(TaiKhoanNguoiDung taiKhoanNguoiDung) {
        this.taiKhoanNguoiDung = taiKhoanNguoiDung;
    }

    public PhongBan getPhongBan() {
        return phongBan;
    }

    public void setPhongBan(PhongBan phongBan) {
        this.phongBan = phongBan;
    }
}
