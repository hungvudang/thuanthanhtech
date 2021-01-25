package com.thuanthanhtech.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thuanthanhtech.entities.Category;
import com.thuanthanhtech.entities.CategoryHelper;
import com.thuanthanhtech.entities.RootCategory;
import com.thuanthanhtech.repositories.CategoryRepository;

@Controller
@RequestMapping("/category")
public class CategoryAdminController {

	@Autowired
	private CategoryRepository cRepository;

	@GetMapping
	public String category(Model m) {
		List<Category> categories = cRepository.findAll();
		m.addAttribute("categories", categories);
		return "admin-pages/category";
	}

	@GetMapping("/create")
	public String createCategory(Model m) {

		Category c = new Category();
		c.setPub(0);
		c.setHot(0);
		c.setParent_id(0);

		// Tạo danh sách danh mục -> danh mục gốc
		// =================================================
		List<Category> categories = cRepository.findAll();
		List<Boolean> visited = null;

		List<RootCategory> root = new ArrayList<RootCategory>();
		root.add(new RootCategory(0, "--- Danh mục gốc ---", 0));

		if (!categories.isEmpty()) {
			visited = new ArrayList<Boolean>();
			for (int i = 0; i < categories.size(); i++) {
				visited.add(false);
			}
			CategoryHelper.getInstant().recusive_categories(categories, visited, 0, "", root);
		}
		// ===================================================================================

		m.addAttribute("root_categories", root);
		m.addAttribute("category", c);
		return "admin-pages/create-category";
	}

	@PostMapping("/save")
	public String saveCategory(@ModelAttribute("category") Category category) {

		if (category.getTitle().isBlank() || category.getTitle().isEmpty() || category.getName().isEmpty()) {
			return "redirect:/category/create";
		}

		// Tạo slug dựa theo tên danh mục
		String slug = SlugConverter.convert(category.getName());
		category.setSlug(slug);
		// ==============================

		cRepository.save(category);
		return "redirect:/category";
	}

	@GetMapping("/detail/{id}")
	public String detailCategory(@PathVariable("id") Integer id, Model m) {
		Optional<Category> opCategory = cRepository.findById(id);
		if (opCategory.isPresent()) {
			Category category = opCategory.get();

			// Tạo danh sách danh mục -> danh mục gốc
			// =================================================
			List<Category> categories = cRepository.findAll();
			List<Boolean> visited = null;

			List<RootCategory> root = new ArrayList<RootCategory>();
			root.add(new RootCategory(0, "--- Danh mục gốc ---", 0));

			if (!categories.isEmpty()) {
				visited = new ArrayList<Boolean>();
				for (int i = 0; i < categories.size(); i++) {
					visited.add(false);
				}
				CategoryHelper.getInstant().recusive_categories(categories, visited, 0, "", root);
			}
			// ===================================================================================

			m.addAttribute("root_categories", root);
			m.addAttribute("category", category);
			return "admin-pages/category-detail";
		}
		return "rediect:/category";
	}

	@PostMapping("/update/{id}")
	public String updateCategory(@PathVariable("id") Integer id, @ModelAttribute("category") Category category) {

		if (category.getName().isBlank() || category.getName().isEmpty()) {
			return "redirect:/category/detail/" + id;
		}

		Category nCategory = cRepository.findById(id).get();

		nCategory.setId(category.getId());
		nCategory.setName(category.getName());
		nCategory.setTitle(category.getTitle());
		nCategory.setParent_id(category.getId());
		nCategory.setHot(category.getHot());
		nCategory.setPub(category.getPub());

		nCategory.setParent_id(category.getParent_id());

		// Tạo slug dựa theo tên danh mục
		String slug = SlugConverter.convert(category.getName());
		category.setSlug(slug);
		// ==============================

		cRepository.save(nCategory);
		return "redirect:/category";
	}

	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable("id") Integer id) {
		Optional<Category> opCategory = cRepository.findById(id);
		if (opCategory.isPresent()) {
			cRepository.deleteById(id);
		}
		return "redirect:/category";
	}

}
