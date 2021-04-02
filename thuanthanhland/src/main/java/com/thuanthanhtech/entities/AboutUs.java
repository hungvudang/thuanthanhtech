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
@Table(name = "aboutus")
public class AboutUs {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name ="title", columnDefinition = "VARCHAR(255) NOT NULL" )
	private String title;
	
	@Column(name ="content", columnDefinition = "VARCHAR(255) NOT NULL")
	private String content;
	
	@Column(name = "thumbnail", columnDefinition = "TEXT")
	private String thumbnail;
	
	@Column(name="pub", columnDefinition = "TINYINT(4) DEFAULT 1")
	private Integer pub;
	
	@Column(name ="created_at")
	private LocalDateTime created_at;
	
	@Column(name ="updated_at")
	private LocalDateTime updated_at;

	public AboutUs(Integer id, String title, String content, String thumbnail) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.thumbnail =thumbnail ;
	}
	
	public Integer getPub() {
		return pub;
	}

	public void setPub(Integer pub) {
		this.pub = pub;
	}

	public AboutUs() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String image) {
		this.thumbnail = image;
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
		this.updated_at= LocalDateTime.now();
	}
	
}
