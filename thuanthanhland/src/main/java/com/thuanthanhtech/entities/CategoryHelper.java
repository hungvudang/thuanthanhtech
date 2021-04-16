package com.thuanthanhtech.entities;

import java.util.ArrayList;
import java.util.List;

public class CategoryHelper {
	
	
	public static void getCategoryTree(List<Category> categories, Item<Category> root, Category target ) {
		List<Boolean> visited = new ArrayList<Boolean>();
		
		root.setId(target.getId());
		root.setSelf(target);
		root.setChilds(new ArrayList<Item<Category>>());
		
		categories.stream().forEach((it)->{
			visited.add(false);
		});
		
		recursive(categories, visited, root);
		
	}
	
	
	private static void recursive(List<Category> categories, List<Boolean> visited, Item<Category> root) {
		for (int i = 0; i < categories.size(); i++) {
			if (visited.get(i) == false && categories.get(i).getParentId() == root.getId()) {
				visited.set(i, true);
				Item<Category> child = new Item<Category>(categories.get(i).getId(), categories.get(i),
						new ArrayList<Item<Category>>());
				root.getChilds().add(child);
				recursive(categories, visited, child);
			}
		}
	}
}
