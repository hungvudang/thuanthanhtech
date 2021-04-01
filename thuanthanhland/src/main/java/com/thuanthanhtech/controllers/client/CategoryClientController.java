package com.thuanthanhtech.controllers.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.thuanthanhtech.entities.Category;
import com.thuanthanhtech.entities.CategoryHelper;
import com.thuanthanhtech.entities.Item;
import com.thuanthanhtech.repositories.CategoryRepository;

@Controller
public class CategoryClientController {

	@Autowired
	private CategoryRepository cRepository;

	public void categories(Model m) {
		List<Category> categories = cRepository.findByPub(1, Sort.by(Sort.Direction.ASC, "parentId", "sort"));

		Item<Category> root = new Item<Category>();
		Category cate = new Category();
		cate.setId(0);
		cate.setName("Danh mục gốc");

		CategoryHelper.getCategoryTree(categories, root, cate);
		m.addAttribute("rootCate", root);
	}
}
