package com.thuanthanhtech.entities;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "news")

public class News {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", columnDefinition = "VARCHAR(255) NOT NULL", unique = true)
	@NotBlank(message = "Tên không được để trống")
	private String name;

	@NotBlank(message = "Tiêu đề không được để trống")
	@Column(name = "title", columnDefinition = "VARCHAR(255) NOT NULL")
	private String title;

	@Column(name = "slug", columnDefinition = "VARCHAR(255) NOT NULL")
	@NotBlank(message = "Slug không được để trống")
	private String slug;

	@Column(name = "description", columnDefinition = "TEXT NOT NULL")
	@NotBlank(message = "Mô tả không được để trống")
	private String description;

	@Column(name = "content", columnDefinition = "LONGTEXT")
	private String content;

	@Column(name = "thumbnail", columnDefinition = "TEXT")
	private String thumbnail;

	@ManyToOne(targetEntity = Category.class, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;

	@Column(name = "hot", columnDefinition = "TINYINT(4) DEFAULT 0")
	private Integer hot;

	@Column(name = "public", columnDefinition = "TINYINT(4) DEFAULT 1")
	private Integer pub;

	@Column(name = "created_at")
	private LocalDateTime createdAt;
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	
	public News() {
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Integer getHot() {
		return hot;
	}

	public void setHot(Integer hot) {
		this.hot = hot;
	}

	public Integer getPub() {
		return pub;
	}

	public void setPub(Integer pub) {
		this.pub = pub;
	}

	

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatetAt) {
		this.updatedAt = updatetAt;
	}

	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
