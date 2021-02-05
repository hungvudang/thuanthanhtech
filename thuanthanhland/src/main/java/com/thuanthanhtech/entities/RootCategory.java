package com.thuanthanhtech.entities;

import java.io.Serializable;

public class RootCategory implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;

	public RootCategory() {
		super();
	}

	public RootCategory(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
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

	@Override
	public boolean equals(Object o) {

		if (o instanceof RootCategory) {
			RootCategory rc = (RootCategory) o;
			if (this.id == rc.id)
				return true;
		}
		
		return false;
		
	}
}
