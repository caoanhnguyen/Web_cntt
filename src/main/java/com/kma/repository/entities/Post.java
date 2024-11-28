package com.kma.repository.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="bai_viet")
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer postId;
	
	@Column(name="title", columnDefinition = "LONGTEXT")
	private String title;
	
	@Column(name="content", columnDefinition = "LONGTEXT")
	private String content;
	
	@Column(name="createAt")
	private Date createAt;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "authorId")
	private NhanVien nhanVien;
	
	@OneToMany(mappedBy = "post")
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
		return postId;
	}
	public void setPost_id(Integer post_id) {
		this.postId = post_id;
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
	public Integer getPostId() { return postId; }
	public void setPostId(Integer postId) { this.postId = postId; }
}
