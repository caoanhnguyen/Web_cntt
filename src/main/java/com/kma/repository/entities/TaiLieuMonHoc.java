package com.kma.repository.entities;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name="tai_lieu_mon_hoc")
public class TaiLieuMonHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer docId;

    @Column(name="description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name="fileCode")
    private String fileCode;

    @Column(name="createAt")
    private Date createAt;

    //Config relation to mon_hoc
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monHocId")
    private MonHoc monHoc;

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public MonHoc getMonHoc() {
        return monHoc;
    }

    public void setMonHoc(MonHoc monHoc) {
        this.monHoc = monHoc;
    }
}
