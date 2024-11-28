package com.kma.repository.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "su_kien")
public class SuKien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;

    @Column(name="eventName")
    private String eventName;

    @Column(name="description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name="startAt")
    private Date startAt;

    @Column(name="endAt")
    private Date endAt;

    @Column(name="location")
    private String location;

    @Column(name="organizedBy")
    private String organizedBy;

    @Column(name="createAt")
    private Date createAt;

    //Config relation to tai_nguyen
    @OneToMany(mappedBy = "event")
    private List<TaiNguyen> taiNguyenList;

    //Config relation to dang_ki_su_kien
    @OneToMany(mappedBy = "event")
    private List<DangKySuKien> dkskList;

    public List<DangKySuKien> getDkskList() {
        return dkskList;
    }

    public void setDkskList(List<DangKySuKien> dkskList) {
        this.dkskList = dkskList;
    }

    public List<TaiNguyen> getTaiNguyenList() {
        return taiNguyenList;
    }

    public void setTaiNguyenList(List<TaiNguyen> taiNguyenList) {
        this.taiNguyenList = taiNguyenList;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getOrganizedBy() {
        return organizedBy;
    }

    public void setOrganizedBy(String organizedBy) {
        this.organizedBy = organizedBy;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
}
