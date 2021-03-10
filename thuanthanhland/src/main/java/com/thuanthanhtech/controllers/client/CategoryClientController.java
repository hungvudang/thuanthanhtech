package com.thuanthanhtech.controllers.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuanthanhtech.entities.Category;
import com.thuanthanhtech.entities.CategoryHelper;
import com.thuanthanhtech.entities.Item;
import com.thuanthanhtech.repositories.CategoryRepository;

@RestController
@RequestMapping("/api/category")
@CrossOrigin("*")
public class CategoryClientController {
	
	@Autowired
	private CategoryRepository cRepository;
	
	@GetMapping
	public ResponseEntity<?> findAll(){
		List<Category> categories = cRepository.findByPub(1,Sort.by(Sort.Direction.ASC, "parentId", "sort"));
		
		Item<Category> root = new Item<Category>();
		Category cate = new Category();
		cate.setId(0);
		cate.setName("Danh mục gốc");

		CategoryHelper.getCategoryTree(categories, root, cate);
		
		return ResponseEntity.ok().body(root);
	}
}
