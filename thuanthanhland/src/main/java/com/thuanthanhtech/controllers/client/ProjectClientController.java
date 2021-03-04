package com.thuanthanhtech.controllers.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thuanthanhtech.entities.Project;
import com.thuanthanhtech.repositories.ProjectPagingAndSortRepository;

@Controller
@RequestMapping("/du-an")
public class ProjectClientController {
	
	@Autowired
	private ProjectPagingAndSortRepository pRepository;
	
	@GetMapping
	public String project(Model m) {
		return "redirect:/du-an/page/1";
	}
	
	@GetMapping("/page/{index}")
	public String nextPage(@PathVariable("index") Integer index, Model m) {
		Pageable pageable = PageRequest.of(index - 1, 8);
		Page<Project> page = pRepository.findByPub(1, pageable);
		
		List<Project> projects = page.getContent();
		int currentPage = index;
		int totalPages = page.getTotalPages();
		int numOfElements = page.getNumberOfElements();
		
		m.addAttribute("projects", projects);
		m.addAttribute("currentPage", currentPage);
		m.addAttribute("totalPages", totalPages);
		m.addAttribute("numOfElements", numOfElements);
		
		return "public-pages/project";
	}
	
	@GetMapping("/detail/{slug}")
	public String projectDetail(@PathVariable("slug") String slug) {
		
		return "public-pages/project-single";
	}
}
