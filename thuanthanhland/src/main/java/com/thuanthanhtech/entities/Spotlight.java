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

@Entity
@Table(name = "spotlight")
public class Spotlight {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name", nullable = false)
	@NotBlank(message = "Tên không hợp lệ")
	private String name;
	
	@Column(name = "title", nullable = false)
	@NotBlank(message = "Tiêu đề không hợp lệ")
	private String title;
	
	@Column(name = "image", nullable = false)
	private String image;
	
	@Column(name = "sort", nullable = false, unique = true)
	private Integer sort;
	
	@Column(name = "public", columnDefinition = "TINYINT(1) DEFAULT 1")
	private Integer pub;
	
	@Column(name = "description", columnDefinition = "LONGTEXT", nullable = false)
	@NotBlank(message = "Mô tả không hợp lệ")
	private String description;
	
	
	private LocalDateTime created_at;
	private LocalDateTime updated_at;

	public Spotlight() {
		super();
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	

	public Integer getSort() {
		return sort;
	}


	public void setSort(Integer sort) {
		this.sort = sort;
	}


	public Integer getPub() {
		return pub;
	}


	public void setPub(Integer pub) {
		this.pub = pub;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
