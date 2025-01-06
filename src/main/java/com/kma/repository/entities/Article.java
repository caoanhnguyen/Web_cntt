package com.kma.repository.entities;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer articleId;

    @Column(name="title", columnDefinition = "LONGTEXT")
    private String title;

    @Column(name="content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name="createAt")
    private Date createAt;

    @ManyToOne
    @JoinColumn(name = "menuItemId", nullable = false)
    private MenuItem menuItem;

    @OneToMany(mappedBy = "article")
    private List<TaiNguyen> taiNguyenList = new ArrayList<>();

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
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

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public List<TaiNguyen> getTaiNguyenList() {
        return taiNguyenList;
    }

    public void setTaiNguyenList(List<TaiNguyen> taiNguyenList) {
        this.taiNguyenList = taiNguyenList;
    }
}
