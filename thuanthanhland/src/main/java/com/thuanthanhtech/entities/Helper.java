package com.thuanthanhtech.entities;

import java.util.ArrayList;
import java.util.List;

public class Helper {
	public static String FILE_SEPARTOR = "/";
	public static String NO_IMAGE_MEDIUM_PNG = "/admin-static/images/no-thumbnail-medium.png";

	public static void recursiveMenu(List<Category> categories, List<Boolean> visited, Items<Category> rootCategory) {
		for (int i = 0; i < categories.size(); i++) {
			if (visited.get(i) == false && categories.get(i).getParentId() == rootCategory.getId()) {
				visited.set(i, true);
				Items<Category> child = new Items<Category>(categories.get(i).getId(), categories.get(i),
						new ArrayList<Items<Category>>());
				rootCategory.getChilds().add(child);
				recursiveMenu(categories, visited, child);
			}
		}
	}
}
