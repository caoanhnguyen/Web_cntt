package com.kma.repository.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user_account")
public class UserAccount {

    @Id
    @Column(name="accountId", nullable = false)
    private Integer accountId;

    @Column(name="userName")
    private String matKhau;

    @Column(name="password")
    private String password;

    @Column(name="isActive")
    private Integer isActive;

    // Config relation to sinh_vien
    @OneToOne(mappedBy = "userAccount")
    private SinhVien sinhVien;

    // Config relation to sinh_vien
    @OneToOne(mappedBy = "userAccount")
    private NhanVien nhanVien;

    // Config relation to role
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "accountId", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private List<Role> roleList = new ArrayList<>();

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public SinhVien getSinhVien() {
        return sinhVien;
    }

    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }
}
