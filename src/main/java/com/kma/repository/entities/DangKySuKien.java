package com.kma.repository.entities;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "dang_ky_su_kien",uniqueConstraints = @UniqueConstraint(columnNames = {"maSinhVien", "eventId"}))
public class DangKySuKien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "regisDate")
    private Date regisDate;

    @Column(name = "status")
    private Integer status;

    // Config relation to sinh_vien
    @ManyToOne
    @JoinColumn(name = "maSinhVien", nullable = false)
    private SinhVien sinhVien;

    //Config relation to su_kien
    @ManyToOne
    @JoinColumn(name = "eventId", nullable = false)
    private SuKien event;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getRegisDate() {
        return regisDate;
    }

    public void setRegisDate(Date regisDate) {
        this.regisDate = regisDate;
    }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }

    public SinhVien getSinhVien() {
        return sinhVien;
    }

    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }

    public SuKien getEvent() {
        return event;
    }

    public void setEvent(SuKien event) {
        this.event = event;
    }
}
