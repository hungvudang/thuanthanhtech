package com.thuanthanhtech.entities;

public class RootCategory {
	private Integer id;
	private String name;
	private Integer parent_id;

	public RootCategory() {
		super();
	}

	public RootCategory(Integer id, String name, Integer parent_id) {
		super();
		this.id = id;
		this.name = name;
		this.parent_id = parent_id;
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

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}
	
	

}
