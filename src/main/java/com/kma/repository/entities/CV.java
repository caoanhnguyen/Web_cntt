package com.kma.repository.entities;

import jakarta.persistence.*;

@Entity
@Table(name="cv")
public class CV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer CVId;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    // Quan hệ 1-1 với NhanVien
    @OneToOne
    @JoinColumn(name = "nvId", referencedColumnName = "idUser")
    private NhanVien nhanVien;

    public Integer getCVId() {
        return CVId;
    }

    public void setCVId(Integer CVId) {
        this.CVId = CVId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }
}
