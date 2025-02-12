package com.kma.repository.entities;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "slider")
public class Slider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sliderId;

    @Column(name="title", columnDefinition = "LONGTEXT")
    private String title;

    @Column(name="content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name="createAt")
    private Date createAt;

    public Integer getSliderId() {
        return sliderId;
    }

    public void setSliderId(Integer sliderId) {
        this.sliderId = sliderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
