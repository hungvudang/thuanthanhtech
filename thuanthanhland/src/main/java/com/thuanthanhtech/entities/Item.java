package com.thuanthanhtech.entities;

import java.util.List;

public class Item <T>{
	private Integer id;
	private T self;
	private List<Item<T>> childs;
	
	
	
	public Item() {
		super();
	}


	public Item(Integer id, T self, List<Item<T>> childs) {
		super();
		this.id = id;
		this.self = self;
		this.childs = childs;
	}

	
	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public T getSelf() {
		return self;
	}


	public void setSelf(T self) {
		this.self = self;
	}


	public List<Item<T>> getChilds() {
		return childs;
	}


	public void setChilds(List<Item<T>> childs) {
		this.childs = childs;
	}
	
	
	
	
	
	
}
