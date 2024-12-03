package com.kma.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.util.List;

public class suKienDTO {
    private Integer eventId;
    private String eventName;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")  // Định dạng ngày tháng năm
    private Date startAt;
    @JsonFormat(pattern = "yyyy-MM-dd")  // Định dạng ngày tháng năm
    private Date endAt;
    private String location;
    @JsonFormat(pattern = "yyyy-MM-dd")  // Định dạng ngày tháng năm
    private Date createAt;
    private String organizedBy;
    private List<fileDTO> fileDTOList;

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public List<fileDTO> getFileDTOList() {
        return fileDTOList;
    }

    public void setFileDTOList(List<fileDTO> fileDTOList) {
        this.fileDTOList = fileDTOList;
    }

}
