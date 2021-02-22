package com.thuanthanhtech.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thuanthanhtech.entities.Project;
import com.thuanthanhtech.repositories.ProjectRepository;
@Controller
@RequestMapping("/project")
public class ProjectAdminController {
	@Autowired
	private ProjectRepository pRepository;
	
	
	@GetMapping
	public String Project (Model m) {
		List<Project> projects= pRepository.findAll();
		m.addAttribute("projects", projects);
		m.addAttribute("active_project", true);
		return "admin-pages/project";
		
	}

	
	
	

	
	
	
}
