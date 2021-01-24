package com.thuanthanhtech.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

	@Column(columnDefinition = "varchar(255) not null", name = "title")
	private String title;

	@Column(name = "slug")
	private String slug;

	@Column(name = "parent_id")
	private Integer parent_id;

	@Column(name = "hot", columnDefinition = "TINYINT(4) DEFAULT 0")
	private Integer hot;

	@Column(name = "public", columnDefinition = "TINYINT(4) DEFAULT 1")
	private Integer pub;

	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "category", targetEntity = News.class)
	List<News> newses;


	public Category() {
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
	
	public List<News> getNewses() {
		return newses;
	}

	public void setNewses(List<News> newses) {
		this.newses = newses;
		
		for (News n : newses) {
			n.setCategory(this);
		}
	}
	
	@PrePersist
	public void prePersist() {
		this.created_at = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updated_at = LocalDateTime.now();
	}


	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", title=" + title + ", slug=" + slug + ", parent_id="
				+ parent_id + ", hot=" + hot + ", public=" + pub + ", created_at=" + created_at + ", updated_at="
				+ updated_at + "]";
	}
	
	

}
