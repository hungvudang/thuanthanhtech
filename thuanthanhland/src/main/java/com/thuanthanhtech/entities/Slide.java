package com.thuanthanhtech.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(name = "slides")
public class Slide {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "image", columnDefinition = "VARCHAR(255) NOT NULL")
	private String image;
	
	@Column(name = "sort", columnDefinition = "INT NOT NULL UNIQUE")
	private Integer sort;
	
	@Column(name = "public", columnDefinition = "TINYINT(4) DEFAULT 1")
	private Integer _public;
	
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	
	public Slide() {
		super();
	}

	public Slide(Integer id, String name, String title, String image, Integer sort, Integer _public,
			LocalDateTime created_at, LocalDateTime updated_at) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
		this.image = image;
		this.sort = sort;
		this._public = _public;
		this.created_at = created_at;
		this.updated_at = updated_at;
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

	public Integer get_public() {
		return _public;
	}

	public void set_public(Integer _public) {
		this._public = _public;
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
