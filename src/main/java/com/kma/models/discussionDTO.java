package com.kma.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.util.List;

public class discussionDTO {
    private Integer discussionId;
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")  // Định dạng ngày tháng năm
    private Date createAt;
    private userDTO author;
    private List<tagDTO> tagDTOList;
    private voteDTO voteDTO;
    private Integer answerQuantity;
    private boolean isOwner;

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public Integer getAnswerQuantity() {
        return answerQuantity;
    }

    public void setAnswerQuantity(Integer answerQuantity) {
        this.answerQuantity = answerQuantity;
    }

    public com.kma.models.voteDTO getVoteDTO() {
        return voteDTO;
    }

    public void setVoteDTO(com.kma.models.voteDTO voteDTO) {
        this.voteDTO = voteDTO;
    }

    public List<tagDTO> getTagDTOList() {
        return tagDTOList;
    }

    public void setTagDTOList(List<tagDTO> tagDTOList) {
        this.tagDTOList = tagDTOList;
    }

    public Integer getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(Integer discussionId) {
        this.discussionId = discussionId;
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

    public userDTO getAuthor() {
        return author;
    }

    public void setAuthor(userDTO author) {
        this.author = author;
    }
}
