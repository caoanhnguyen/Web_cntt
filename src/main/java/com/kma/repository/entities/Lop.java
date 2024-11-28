package com.kma.repository.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "lop")
    private List<SinhVien> svList = new ArrayList<>();

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

    public List<SinhVien> getSvList() {
        return svList;
    }

    public void setSvList(List<SinhVien> svList) {
        this.svList = svList;
    }
}
