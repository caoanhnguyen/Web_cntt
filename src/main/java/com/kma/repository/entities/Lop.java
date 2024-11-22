package com.kma.repository.entities;

import jakarta.persistence.*;

@Entity
@Table(name="lop")
public class Lop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLop;

    @Column(name="tenLop")
    private String tenLop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idChuNhiem")
    private NhanVien chuNhiem;

    public Integer getIdLop() {
        return idLop;
    }

    public void setIdLop(Integer idLop) {
        this.idLop = idLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public NhanVien getChuNhiem() {
        return chuNhiem;
    }

    public void setChuNhiem(NhanVien chuNhiem) {
        this.chuNhiem = chuNhiem;
    }
}
