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
@Table(name = "year_slider")
public class YearSlider {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name", columnDefinition = "VARCHAR(255) NOT NULL" )
	@NotBlank(message = "Tên không được để trống")
	private String name;
	
	@NotBlank(message = "Ảnh không được để trống")
	@Column(name = "thumbnail", columnDefinition = "VARCHAR(255)", nullable = false)
	private String thumbnail;
	
	@NotBlank(message ="Nội dung không được để trống")
	@Column(name= "content", columnDefinition = "VARCHAR(255)",nullable = false)
	private String content;
	
	@Column(name = "hot", columnDefinition = "TINYINT(4) DEFAULT 0", nullable = false)
	private Integer hot;
	
	@Column(name ="public", columnDefinition = "TINYINT(4) DEFAULT 1", nullable = false)
	private Integer pub;
	
	@Column(name= "created_at")
	private LocalDateTime createdAt;
	
	@Column(name ="updated_at")
	private LocalDateTime updatedAt;

	

	public YearSlider(Integer id, @NotBlank(message = "Tên không được để trống") String name,
			@NotBlank(message = "Ảnh không được để trống") String thumbnail,
			@NotBlank(message = "Nội dung không được để trống") String content, Integer hot, Integer pub,
			LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.name = name;
		this.thumbnail = thumbnail;
		this.content = content;
		this.hot = hot;
		this.pub = pub;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public YearSlider(Integer id, String name, String thumbnail, String content) {
		super();
		this.id = id;
		this.name = name;
		this.thumbnail = thumbnail;
		this.content = content;
	}

	public YearSlider() {
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

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
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

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	@PrePersist
	public void prePersist() {
		this.createdAt=LocalDateTime.now();
	}
	@PreUpdate
	public void preUpdate() {
		this.updatedAt= LocalDateTime.now();
	}
}
