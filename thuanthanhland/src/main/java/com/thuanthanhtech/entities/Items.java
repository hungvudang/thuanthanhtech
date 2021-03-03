package com.thuanthanhtech.entities;

import java.util.List;

public class Items <T>{
	private Integer id;
	private T parent;
	private List<Items<T>> childs;
	
	
	
	public Items() {
		super();
	}


	public Items(Integer id, T parent, List<Items<T>> childs) {
		super();
		this.id = id;
		this.parent = parent;
		this.childs = childs;
	}

	
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public T getParent() {
		return parent;
	}


	public void setParent(T parent) {
		this.parent = parent;
	}


	public List<Items<T>> getChilds() {
		return childs;
	}


	public void setChilds(List<Items<T>> childs) {
		this.childs = childs;
	}
	
	
	
	
	
	
}
