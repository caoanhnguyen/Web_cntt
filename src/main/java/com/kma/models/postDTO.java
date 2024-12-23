package com.kma.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

public class postDTO {
	
	private Integer post_id; 
	private String title;
	private String content;
	@JsonFormat(pattern = "yyyy-MM-dd")  // Định dạng ngày tháng năm
	private Date create_at;
	private List<fileDTO> file_dto;
	private String author;
	@Getter
	@Setter
	private boolean isOwner;

	public Integer getPost_id() {
		return post_id;
	}
	public void setPost_id(Integer post_id) {
		this.post_id = post_id;
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
	public Date getCreate_at() {
		return create_at;
	}
	public void setCreate_at(Date create_at) {
		this.create_at = create_at;
	}
	public List<fileDTO> getFile_dto() {
		return file_dto;
	}
	public void setFile_dto(List<fileDTO> file_dto) {
		this.file_dto = file_dto;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	
}
