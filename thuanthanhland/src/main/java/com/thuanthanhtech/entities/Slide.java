package com.thuanthanhtech.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "slides")
public class Slide {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	@NotBlank(message = "Tiêu đề không được để trống")
	@Column(name = "title")
	private String title;
	
	@NotBlank(message = "Tên không được để trống")
	@Column(name = "description")
	private String description;
	
	
	@Column(name = "image", columnDefinition = "VARCHAR(255) NOT NULL")
	private String image;
	
	@Column(name = "sort", columnDefinition = "INT NOT NULL UNIQUE")
	@NotNull(message = "Thứ tự hiển thị không được để trống")
	@Min(value = 0, message = "Thứ tự hiển thị không không hợp lệ")
	private Integer sort;
	
	@Column(name = "public", columnDefinition = "TINYINT(4) DEFAULT 1")
	private Integer pub;
	
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	
	public Slide() {
		super();
	}

	public Slide(Integer id, String description, String title, String image, Integer sort, Integer pub,
			LocalDateTime created_at, LocalDateTime updated_at) {
		super();
		this.id = id;
		this.description = description;
		this.title = title;
		this.image = image;
		this.sort = sort;
		this.pub = pub;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	public void preUpdate() {
		this.updated_at = LocalDateTime.now();
	}
	
	
	
	
}
