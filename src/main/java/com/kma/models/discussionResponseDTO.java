package com.kma.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.sql.Date;

public class discussionResponseDTO {
    private Integer discussionId;
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")  // Định dạng ngày tháng năm
    private Date createAt;
    private String discussionStatus;
    private userDTO author_DTO;
    private BigDecimal score;
    private voteDTO voteDTO;
    private Long answerQuantity;

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getDiscussionStatus() {
        return discussionStatus;
    }

    public void setDiscussionStatus(String discussionStatus) {
        this.discussionStatus = discussionStatus;
    }

    public Long getAnswerQuantity() {
        return answerQuantity;
    }

    public void setAnswerQuantity(Long answerQuantity) {
        this.answerQuantity = answerQuantity;
    }

    public com.kma.models.voteDTO getVoteDTO() {
        return voteDTO;
    }

    public void setVoteDTO(com.kma.models.voteDTO voteDTO) {
        this.voteDTO = voteDTO;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public userDTO getAuthor_DTO() {
        return author_DTO;
    }

    public void setAuthor_DTO(userDTO author_DTO) {
        this.author_DTO = author_DTO;
    }
}
