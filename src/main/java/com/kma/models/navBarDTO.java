package com.kma.models;

import java.util.List;

public class navBarDTO {
        private Integer id;
        private String title;
    private String slug;
    private Boolean isDeleted;
    private List<navBarDTO> children;

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public List<navBarDTO> getChildren() {
        return children;
    }

    public void setChildren(List<navBarDTO> children) {
        this.children = children;
    }
}
