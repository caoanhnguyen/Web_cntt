package com.kma.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

public class postResponseDTO {
    private Integer postId;
    private String title;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd")  // Định dạng ngày tháng năm
    private Date createAt;
    private String authorName;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
