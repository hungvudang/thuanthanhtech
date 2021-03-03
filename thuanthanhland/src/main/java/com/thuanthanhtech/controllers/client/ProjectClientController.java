package com.thuanthanhtech.controllers.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thuanthanhtech.entities.Project;
import com.thuanthanhtech.repositories.ProjectRepository;

@Controller
@RequestMapping("/du-an")
public class ProjectClientController {
	
	@Autowired
	private ProjectRepository pRepository;
	
	@GetMapping
	public String project(Model m) {
		List<Project> projects = pRepository.findByPub(1);
		
		m.addAttribute("projects", projects);
		return "public-pages/project";
	}
	
	@GetMapping("/detail/{slug}")
	public String projectDetail(@PathVariable("slug") String slug) {
		
		return "public-pages/project-single";
	}
}
