package com.kma.repository.entities;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tai_nguyen")
public class TaiNguyen {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer resourceId;
	
	@Column(name="description", columnDefinition = "LONGTEXT")
	private String description;
	
	@Column(name="fileCode")
	private String fileCode;
	
	@Column(name="createAt")
	private Date createAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "postId")
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "eventId")
	private SuKien event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "articleId")
	private Article article;

	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public Integer getResourceId() {
		return resourceId;
	}
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFileCode() {
		return fileCode;
	}
	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) { this.createAt = createAt; }
	public SuKien getEvent() { return event; }
	public void setEvent(SuKien event) { this.event = event; }
}
