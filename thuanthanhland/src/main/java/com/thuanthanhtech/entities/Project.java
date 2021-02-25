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

@Entity
@Table(name="projects")
public class Project {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="title", nullable=false)
	private String title;
	
	@Column(name="content", columnDefinition = "TEXT NOT NULL")
	private String content;
	
	@Column(name="description", columnDefinition = "LONGTEXT NOT NULL")
	private String description;
	
	@Column(name="image", columnDefinition = "TEXT")
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
	
	public Project(Integer id, String name, String title, String content, String description, String image, Integer pub,
			Integer hot, LocalDateTime created_at, LocalDateTime updated_at) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
		this.content = content;
		this.description = description;
		this.image = image;
		this.pub = pub;
		this.hot = hot;
		this.created_at = created_at;
		this.updated_at = updated_at;
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
	
	@PrePersist
	public void prePersist() {
		this.created_at = LocalDateTime.now();
	}
	@PreUpdate
	public void preUpdate() {
		this.updated_at = LocalDateTime.now();
	}
	
	
	
}
