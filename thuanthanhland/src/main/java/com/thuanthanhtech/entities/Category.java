package com.thuanthanhtech.entities;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(columnDefinition = "varchar(255) not null" ,name = "title")
	private String title;
	
	@Column(name = "slug")
	private String slug;
	
	@Column(name = "parent_id")
	private Integer parent_id;
	
	@Column(name = "hot", columnDefinition = "TINYINT(1) DEFAULT 0")
	private Integer hot;
	
	@Column(name = "public", columnDefinition = "TINYINT(1) DEFAULT 1")
	private Integer _public;
	
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private Collection<News> news;
	
	public Category() {
		super();
	}

	public Category(Integer id, String name, String title, String slug, Integer parent_id, Integer hot, Integer _public,
			LocalDateTime created_at, LocalDateTime updated_at) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
		this.slug = slug;
		this.parent_id = parent_id;
		this.hot = hot;
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

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}

	public Integer getHot() {
		return hot;
	}

	public void setHot(Integer hot) {
		this.hot = hot;
	}

	public Integer getPublic() {
		return _public;
	}

	public void setPublic(Integer _public) {
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
	
	@PreUpdate
	public void preUpdate() {
		this.updated_at = LocalDateTime.now();
	}
	
	
	
	
	
}
