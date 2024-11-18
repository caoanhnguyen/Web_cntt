package com.kma.repository.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="bai_viet")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer post_id;
	
	@Column(name="title", columnDefinition = "LONGTEXT")
	private String title;
	
	@Column(name="content", columnDefinition = "LONGTEXT")
	private String content;
	
	@Column(name="create_at")
	private Date create_at;
	
//	@Column(name="author_id")
//	private Integer author_id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id")
	private NhanVien nhanVien;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
	private List<TaiNguyen> taiNguyenList = new ArrayList<>();
	
	
	public List<TaiNguyen> getTaiNguyenList() {
		return taiNguyenList;
	}
	public void setTaiNguyenList(List<TaiNguyen> taiNguyenList) {
		this.taiNguyenList = taiNguyenList;
	}
	public NhanVien getNhanVien() {
		return nhanVien;
	}
	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}
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

//	public Integer getAuthor_id() {
//		return author_id;
//	}
//	public void setAuthor_id(Integer author_id) {
//		this.author_id = author_id;
//	}
	
	
}
