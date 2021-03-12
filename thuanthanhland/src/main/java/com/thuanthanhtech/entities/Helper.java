package com.thuanthanhtech.entities;

import java.util.List;
import java.util.Optional;

import com.thuanthanhtech.repositories.CategoryRepository;

public class Helper {
	public static String FILE_SEPARTOR = "/";
	public static String NO_IMAGE_MEDIUM_PNG = "/admin-static/images/no-thumbnail-medium.png";
	
	public static void getBreadcrumb(Category cate, CategoryRepository cRepository, List<String> targetBreadcrumb) {

		Optional<Category> opCate = cRepository.findByParentId(cate.getParentId());

		if (opCate.isPresent()) {
			Category rootCate = opCate.get();
			targetBreadcrumb.add(rootCate.getName());
			getBreadcrumb(rootCate, cRepository, targetBreadcrumb);
		} else {
			targetBreadcrumb.add(null);
		}
	}
}
