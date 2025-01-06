package com.kma.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;
import java.util.List;

public class articleDTO {
    private Integer articleId;
    private String title;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")  // Định dạng ngày tháng năm
    private Date createAt;
    private menuItemDTO menuDTO;
    private List<fileDTO> file_dto;

    public menuItemDTO getMenuDTO() {
        return menuDTO;
    }

    public void setMenuDTO(menuItemDTO menuDTO) {
        this.menuDTO = menuDTO;
    }

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

    public List<fileDTO> getFile_dto() {
        return file_dto;
    }

    public void setFile_dto(List<fileDTO> file_dto) {
        this.file_dto = file_dto;
    }
}
