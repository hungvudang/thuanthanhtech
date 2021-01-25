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

@Entity
@Table(name = "news")

public class News {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", columnDefinition = "VARCHAR(255) NOT NULL")
	private String name;

	@Column(name = "title", columnDefinition = "VARCHAR(255) NOT NULL")
	private String title;

	@Column(name = "slug", columnDefinition = "VARCHAR(255) NOT NULL")
	private String slug;

	@Column(name = "description", columnDefinition = "LONGTEXT NOT NULL")
	private String description;

	@Column(name = "content", columnDefinition = "TEXT NOT NULL")
	private String content;
	
	@Column(name = "image", columnDefinition = "TEXT")
	private String image;
	
	@ManyToOne(targetEntity = Category.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;

	@Column(name = "hot", columnDefinition = "TINYINT(4) DEFAULT 0")
	private Integer hot;

	@Column(name = "public", columnDefinition = "TINYINT(4) DEFAULT 1")
	private Integer pub;
	
	private LocalDateTime created_at;
	private LocalDateTime updated_at;

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

	
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public LocalDateTime getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}
	
	
	@PrePersist
	public void prePersist() {
		this.created_at = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updated_at = LocalDateTime.now();
	}

}
