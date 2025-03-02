package com.kma.repository.entities;

import com.kma.enums.SubjectCategory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="mon_hoc")
public class MonHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer monHocId;

    @Column(name = "tenMonHoc")
    private String tenMonHoc;

    @Column(name = "moTa", columnDefinition = "LONGTEXT")
    private String moTa;

    @Column(name = "soTinChi")
    private Integer soTinChi;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubjectCategory category;

    //Config relation to mon_hoc
    @OneToMany(mappedBy = "monHoc")
    private List<TaiLieuMonHoc> taiLieuMHList = new ArrayList<>();

    //Config relation to nhan_vien
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "giang_vien_mon_hoc",
            joinColumns = @JoinColumn(name = "monHocId", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "idUser", nullable = false)
    )
    private List<NhanVien> nvList;

    public SubjectCategory getCategory() {
        return category;
    }

    public void setCategory(SubjectCategory category) {
        this.category = category;
    }

    public List<NhanVien> getNvList() {
        return nvList;
    }

    public void setNvList(List<NhanVien> nvList) {
        this.nvList = nvList;
    }

    public List<TaiLieuMonHoc> getTaiLieuMHList() {
        return taiLieuMHList;
    }

    public void setTaiLieuMHList(List<TaiLieuMonHoc> taiLieuMHList) {
        this.taiLieuMHList = taiLieuMHList;
    }

    public Integer getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(Integer soTinChi) {
        this.soTinChi = soTinChi;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTenMonHoc() {
        return tenMonHoc;
    }

    public void setTenMonHoc(String tenMonHoc) {
        this.tenMonHoc = tenMonHoc;
    }

    public Integer getMonHocId() {
        return monHocId;
    }

    public void setMonHocId(Integer monHocId) {
        this.monHocId = monHocId;
    }
}
