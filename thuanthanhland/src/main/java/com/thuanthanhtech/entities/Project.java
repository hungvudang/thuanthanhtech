package com.thuanthanhtech.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@Column(name="thumbnail", columnDefinition = "NVARCHAR(255)", nullable = false)
	private String thumbnail;
	
	@Column(name="public", columnDefinition="TINYINT(4) DEFAULT 1")
	private Integer pub;
	
	@Column(name="hot", columnDefinition="TINYINT(4) DEFAULT 0")
	private Integer hot;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Image.class)
	@JsonIgnore
	private List<Image> images;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = CommentProject.class)
	@JsonIgnore
	private List<CommentProject> comments;
	
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "features")
	@MapKeyColumn(name = "feature_key")
	@Column(name = "feature_value", columnDefinition = "TEXT")
	private Map<String, String> features;
	
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
	
	public String getThumbnail() {
		return thumbnail;
	}


	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public List<Image> getImages() {
		return images;
	}


	public void setImages(List<Image> images) {
		if (images != null) {
			images.parallelStream().forEach((img)->{
				img.setProject(this);
			});
		}
		this.images = images;
	}
	
	public List<CommentProject> getComments() {
		return comments;
	}

	public void setComments(List<CommentProject> comments) {
		if (comments != null) {
			comments.parallelStream().forEach((cmt)->{
				cmt.setProject(this);
			});
		}
		
		this.comments = comments;
	}

	public Map<String, String> getFeatures() {
		return features;
	}


	public void setFeatures(Map<String, String> features) {
		this.features = features;
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