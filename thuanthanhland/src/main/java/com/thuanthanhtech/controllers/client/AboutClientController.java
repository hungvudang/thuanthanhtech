package com.thuanthanhtech.controllers.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thuanthanhtech.entities.Category;
import com.thuanthanhtech.entities.Helper;
import com.thuanthanhtech.repositories.CategoryRepository;

@Controller
@RequestMapping("/ve-chung-toi")
public class AboutClientController {
	
	@Autowired
	private CategoryRepository cRepository;
	
	@GetMapping
	public String about(Model m) {
		
		List<String> targetBreadcrumbs = new ArrayList<String>();
		targetBreadcrumbs.add("Về chúng tôi");
		Category cate = cRepository.findBySlug("ve-chung-toi").get();
		
		Helper.getBreadcrumb(cate, cRepository, targetBreadcrumbs);
		
		m.addAttribute("breadcrumbs", targetBreadcrumbs);
		
		return "/public-pages/about";
	}
}
