package com.thuanthanhtech.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="projects")
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="name", nullable=false)
	@NotBlank(message = "Tên không được để trống")
	private String name;
	
	@Column(name="title", nullable=false)
	@NotBlank(message = "Tiêu để không được để trống")
	private String title;
	
	@Column(name = "slug", columnDefinition = "VARCHAR(255) NOT NULL")
	private String slug;
	
	@Column(name="content", columnDefinition = "LONGTEXT") 
	private String content;
	
	@Column(name="description", columnDefinition = "TEXT NOT NULL")
	@NotNull(message = "Mô tả không hợp lệ")
	@NotBlank(message = "Mô tả không được để trống")
	private String description;
	
	@Column(name="image", columnDefinition = "NVARCHAR(255)", nullable = false)
	private String image;
	
	@Column(name="public", columnDefinition="TINYINT(4) DEFAULT 1")
	private Integer pub;
	
	@Column(name="hot", columnDefinition="TINYINT(4) DEFAULT 0")
	private Integer hot;
	
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	
	
	public Integer getId() {
		return id;
	}
	
	

	public Project() {
		super();
	}
	
	public Integer getPub() {
		return pub;
	}
	public void setPub(Integer pub) {
		this.pub = pub;
	}
	public Integer getHot() {
		return hot;
	}
	public void setHot(Integer hot) {
		this.hot = hot;
	}
	public void setId(int id) {
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	
	
	public String getSlug() {
		return slug;
	}



	public void setSlug(String slug) {
		this.slug = slug;
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
