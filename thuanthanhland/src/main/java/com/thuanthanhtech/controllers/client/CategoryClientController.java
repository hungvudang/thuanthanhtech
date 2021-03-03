package com.thuanthanhtech.controllers.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuanthanhtech.entities.Category;
import com.thuanthanhtech.entities.Helper;
import com.thuanthanhtech.entities.Items;
import com.thuanthanhtech.repositories.CategoryRepository;

@RestController
@RequestMapping("/api/category")
@CrossOrigin("*")
public class CategoryClientController {
	
	@Autowired
	private CategoryRepository cRepository;
	
	@GetMapping
	public ResponseEntity<?> findAll(){
		List<Category> categories = cRepository.findAll(Sort.by(Sort.Direction.ASC, "parentId", "sort"));
		
		Items<Category> rootCategory = new Items<Category>();
		rootCategory.setId(0);
		Category root = new Category();
		root.setId(0);
		root.setName("Danh mục gốc");
		rootCategory.setParent(root);
		rootCategory.setChilds(new ArrayList<Items<Category>>());
		
		List<Boolean> visited = new ArrayList<Boolean>();
		for (int i = 0; i< categories.size(); i++) {
			visited.add(false);
		}
		
		Helper.recursiveMenu(categories, visited, rootCategory);
		
		
		return ResponseEntity.ok().body(rootCategory);
	}
}
