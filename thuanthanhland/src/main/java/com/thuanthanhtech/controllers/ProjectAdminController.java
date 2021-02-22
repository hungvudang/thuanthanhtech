package com.thuanthanhtech.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thuanthanhtech.repositories.CategoryRepository;
import com.thuanthanhtech.repositories.ProjectRepository;

import com.thuanthanhtech.entities.Project;
@Controller
@RequestMapping("/project")
public class ProjectAdminController {
	@Autowired
	private ProjectRepository pRepository;
	
	@Autowired
	private CategoryRepository cpRepository;
	
	@GetMapping
	public String Project (Model m) {
		List<Project> projects= pRepository.findAll();
		m.addAttribute("projects", projects);
		m.addAttribute("active_news", true);
		return "admin-pages/project";
		
	}

	@GetMapping("/create")
	public String createProject(Model m) {
		Project p = new Project();
		 p.setPub(0);
		 p.setHot(1);
		 
		 //tao danh muc -> danh muc gốc
		 
		 List<Project> projects= pRepository.findAll();
		 List<Boolean> visited = null;
		 
		return "";
	}

	
	
	

	
	
	
}
