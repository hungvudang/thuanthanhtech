package com.thuanthanhtech.controllers;

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
import com.thuanthanhtech.repositories.CategoryRepository;

@Controller
@RequestMapping("/category")
public class CategoryController {

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
		m.addAttribute("category", c);
		return "admin-pages/create-category";
	}

	@PostMapping("/save")
	public String saveCategory(@ModelAttribute("category") Category category) {

		if (!category.getTitle().isBlank() || !category.getTitle().isEmpty()) {
			cRepository.save(category);
		}
		return "redirect:/category";
	}

	@GetMapping("/detail/{id}")
	public String detailCategory(@PathVariable("id") Integer id, Model m) {
		Optional<Category> opCategory = cRepository.findById(id);
		if (opCategory.isPresent()) {
			Category category = opCategory.get();
			m.addAttribute("category", category);
			return "admin-pages/category-detail";
		}
		return "rediect:/category";
	}

	@PostMapping("/update")
	public String updateCategory(@ModelAttribute("category") Category category) {

		if (category.getName().isBlank() || category.getName().isEmpty()) {
			return "redirect:/category/detail/" + category.getId();
		}
		
		Category nCategory = cRepository.findById(category.getId()).get();
		
		nCategory.setId(category.getId());
		nCategory.setName(category.getName());
		nCategory.setTitle(category.getTitle());
		nCategory.setParent_id(category.getId());
		nCategory.setSlug(category.getSlug());
		nCategory.setHot(category.getHot());
		nCategory.setPub(category.getPub());
	
		
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
