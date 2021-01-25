package com.thuanthanhtech.entities;

import java.util.List;

import org.springframework.context.annotation.Bean;

public class CategoryHelper {
	private static CategoryHelper categoryHelper = null;

	public static CategoryHelper getInstant() {
		if (categoryHelper == null) {
			categoryHelper = new CategoryHelper();
		}

		return categoryHelper;
	}

	@Bean
	public void recusive_categories(List<Category> categories, List<Boolean> visited, Integer parent_id, String rank, List<RootCategory> root) {
		for (int i = 0; i < categories.size(); i++) {
			if ((visited.get(i) == false) && (categories.get(i).getParent_id() == parent_id)) {
				Integer id = categories.get(i).getId();
				String name = rank + categories.get(i).getName();
				root.add(new RootCategory(id, name, parent_id));
				visited.set(i, true);
				recusive_categories(categories, visited, categories.get(i).getId(), rank + "---",root);
			}
		}
	}
}
