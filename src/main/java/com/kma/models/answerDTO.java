package com.kma.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

public class answerDTO {

    private Integer answerId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")  // Định dạng ngày tháng năm
    private Date createAt;
    private userDTO author;
    private voteDTO voteDTO;
    private boolean isOwner;

    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
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

    public userDTO getAuthor() {
        return author;
    }

    public void setAuthor(userDTO author) {
        this.author = author;
    }

    public com.kma.models.voteDTO getVoteDTO() {
        return voteDTO;
    }

    public void setVoteDTO(com.kma.models.voteDTO voteDTO) {
        this.voteDTO = voteDTO;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setIsOwner(boolean owner) {
        isOwner = owner;
    }
}
