package com.thuanthanhtech.controllers.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thuanthanhtech.entities.Project;
import com.thuanthanhtech.entities.ProjectComment;
import com.thuanthanhtech.entities.ProjectHelper;
import com.thuanthanhtech.repositories.ProjectPagingAndSortRepository;

@Controller
@RequestMapping("/du-an")
public class ProjectClientController {

	@Autowired
	private ProjectPagingAndSortRepository pPagingAndSortRepository;
	
	@Autowired
	private CategoryClientController cateClientController;
	
	@Autowired
	private ProjectCommentClientController pCmtClientController;
	
	@GetMapping
	public String project(@RequestParam(name = "pageNumber", required = false) Integer pageNumber, Model m) {
		if (pageNumber == null) {
			return "redirect:/du-an?pageNumber=1";
		}
		
		Pageable pageable = PageRequest.of(pageNumber - 1, 8);
		Page<Project> page = pPagingAndSortRepository.findByPub(1, pageable);
		
		List<Project> projects = page.getContent();
		int currentPage = pageNumber;
		int totalPages = page.getTotalPages();
		int numOfElements = page.getNumberOfElements();
		
		m.addAttribute("projects", projects);
		m.addAttribute("currentPage", currentPage);
		m.addAttribute("totalPages", totalPages);
		m.addAttribute("numOfElements", numOfElements);
		
		m.addAttribute("BASE_PATH_PROJECT_RESOURCE", ProjectHelper.BASE_PATH_PROJECT_RESOURCE);
		m.addAttribute("DIR_IMAGE_DETAILS", ProjectHelper.DIR_IMAGE_DETAILS);
		
		List<String> breadcrumbs = new ArrayList<String>();
		breadcrumbs.add("Dự án");
		m.addAttribute("breadcrumbs", breadcrumbs);
		
		cateClientController.categories(m);
		
		return "public-pages/project";
		
		
	}

	@GetMapping("/{slug}")
	public String projectDetail(@PathVariable("slug") String slug, Model m) {
		
		Optional<Project> opProject = pPagingAndSortRepository.findBySlug(slug);
		if (opProject.isPresent()) {
			
			Project project = opProject.get();
			
			m.addAttribute("project", project);
			m.addAttribute("BASE_PATH_PROJECT_RESOURCE", ProjectHelper.BASE_PATH_PROJECT_RESOURCE);
			m.addAttribute("DIR_IMAGE_DETAILS", ProjectHelper.DIR_IMAGE_DETAILS);
			m.addAttribute("pCommentGlobal", new ProjectComment());
			m.addAttribute("pComment", new ProjectComment());
			
			List<String> breadcrumbs = new ArrayList<String>();
			breadcrumbs.add("Dự án");
			m.addAttribute("breadcrumbs", breadcrumbs);
			
			pCmtClientController.projectComments(m, project);
			
			cateClientController.categories(m);
			
			return "public-pages/project-detail";
		}
		return "redirect:/du-an";
		
	}
}
