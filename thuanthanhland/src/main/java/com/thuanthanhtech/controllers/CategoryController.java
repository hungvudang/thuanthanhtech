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
import org.springframework.web.bind.annotation.RequestParam;

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
		m.addAttribute("category", c);
		return "admin-pages/create-category";
	}

	@PostMapping("/save")
	public String saveCategory(@ModelAttribute("category") Category category, 
			@RequestParam(name = "gridRadiosPublic") int _public,
			@RequestParam(name = "gridRadiosHot") int hot) {
		
		category.setPublic(_public);
		category.setHot(hot);
		
		if (category.getTitle().isBlank() || category.getTitle().isEmpty()) {
			return "redirect:/category";
		}
		
		cRepository.save(category);
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
	public String updateCategory(@ModelAttribute("category") Category oldCategory, 
			@RequestParam(name = "gridRadiosPublic") int _public,
			@RequestParam(name = "gridRadiosHot") int hot) {
		//update
		oldCategory.setPublic(_public);
		oldCategory.setHot(hot);
		
		if (oldCategory.getName().isBlank() || oldCategory.getName().isEmpty()) {
			return "redirect:/category/detail/"+oldCategory.getId();
		}
		Category newCategory = new Category();
		
		newCategory.setId(oldCategory.getId());
		newCategory.setName(oldCategory.getName());
		newCategory.setTitle(oldCategory.getTitle());
		newCategory.setSlug(oldCategory.getSlug());
		newCategory.setParent_id(oldCategory.getId());
		newCategory.setPublic(_public);
		newCategory.setHot(hot);
		cRepository.save(newCategory);
		return "redirect:/category";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable("id") Integer id) {
		
		System.out.println(id);
		Optional<Category> opCategory = cRepository.findById(id);
		if(opCategory.isPresent()) {
			cRepository.deleteById(id);
		}
		return "redirect:/category";
	}
}
