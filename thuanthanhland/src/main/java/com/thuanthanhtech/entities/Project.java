package com.thuanthanhtech.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name="projects")
public class Project {
	
	@Id
	private Integer id;
	
	@Column(name="name",nullable=false)
	private String name;
	
	@Column(name="title")
	private String title;
	
	@Column(name="content", columnDefinition = "LONGTEXT NOT NULL")
	private String content;
	
	@Column(name="description")
	private String description;
	
	@Column(name="thumbnail", columnDefinition = "TEXT")
	private String thumbnail;
	
	@Column(name="status", columnDefinition="TINYINT(4) DEFAULT 0")
	private Integer status;
	
	@Column(name="pub", columnDefinition="TINYINT(4) DEFAULT 0")
	private Integer pub;
	
	@Column(name="hot", columnDefinition="TINYINT(4) DEFAULT 0")
	private Integer hot;
	
	private LocalDateTime created_at;
	private LocalDateTime update_at;
	
	
	public Integer getId() {
		return id;
	}
	public Project(Integer id, String name, String title, String content, String description, String thumbnail,
			Integer status, LocalDateTime created_at, LocalDateTime update_at) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
		this.content = content;
		this.description = description;
		this.thumbnail = thumbnail;
		this.status = status;
		this.created_at = created_at;
		this.update_at = update_at;
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
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
	public LocalDateTime getUpdate_at() {
		return update_at;
	}
	public void setUpdate_at(LocalDateTime update_at) {
		this.update_at = update_at;
	}
	
	@PrePersist
	public void prePersist() {
		this.created_at = LocalDateTime.now();
	}
	@PreUpdate
	public void preUpdate() {
		this.update_at = LocalDateTime.now();
	}
	
	
	
}
