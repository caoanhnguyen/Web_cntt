package com.kma.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Date;

public class taiNguyenDTO {
	
	private String title;
	private String description;
	private String file_path;
	@JsonFormat(pattern = "yyyy-MM-dd")  // Định dạng ngày tháng năm
	private Date create_at;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	public Date getCreate_at() {
		return create_at;
	}
	public void setCreate_at(Date create_at) {
		this.create_at = create_at;
	}
	
	
}
