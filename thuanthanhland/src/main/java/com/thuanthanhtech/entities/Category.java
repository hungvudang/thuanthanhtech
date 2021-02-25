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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "categories")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", nullable = false, unique = true)
	@NotNull(message = "Tên danh mục không hợp lệ")
	@NotBlank(message = "Tên danh mục không được để trống")
	private String name;

	@Column(name = "slug")
	private String slug;

	@Column(name = "parent_id")
	private Integer parent_id;

	@Column(name = "hot", columnDefinition = "TINYINT(4) DEFAULT 0")
	private Integer hot;

	@Column(name = "public", columnDefinition = "TINYINT(4) DEFAULT 1")
	private Integer pub;
	
	@Column(name = "sort", nullable = false ,unique = true)
	@NotNull(message = "Số thứ tự không hợp lệ")
	@Min(value = 0, message = "Số thứ tự không hợp lệ")
	private Integer sort;
	
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "category", targetEntity = News.class)
	List<News> newses;


	public Category() {
	}
	
	

	public Category(Integer id,
			@NotNull(message = "Tên danh mục không hợp lệ") @NotBlank(message = "Tên danh mục không được để trống") String name,
			String slug, Integer parent_id, Integer hot, Integer pub,
			@NotNull(message = "Số thứ tự không hợp lệ") @Min(value = 0, message = "Số thứ tự không hợp lệ") Integer sort,
			LocalDateTime created_at, LocalDateTime updated_at, List<News> newses) {
		super();
		this.id = id;
		this.name = name;
		this.slug = slug;
		this.parent_id = parent_id;
		this.hot = hot;
		this.pub = pub;
		this.sort = sort;
		this.created_at = created_at;
		this.updated_at = updated_at;
		this.newses = newses;
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

	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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

}
