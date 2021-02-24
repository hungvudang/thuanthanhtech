package com.thuanthanhtech.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuanthanhtech.entities.Project;
import com.thuanthanhtech.repositories.ProjectRepository;
@Controller
@RequestMapping("/admin/project")
public class ProjectAdminController {
	
	@Autowired
	private ProjectRepository pRepository;
	
	
	@GetMapping
	public String Project (Model m) {
		List<Project> projects= pRepository.findAll();
		m.addAttribute("projects", projects);
		m.addAttribute("active_project", true);
		return "/admin-pages/project";
		
	}
	
	@GetMapping("/create")
	public String createProject(Model m) {
		Project project = new Project();
		m.addAttribute("project", project);
		m.addAttribute("active-contact", true);
		return "admin-pages/create-contact";
	}

	@PostMapping("/save")
	public String saveProject(@ModelAttribute("contact") Project project, RedirectAttributes ra ) {
		if(project.getName()== null || project.getName().isBlank() || project.getName().isEmpty()  ) {
			ra.addFlashAttribute("error", "Tạo dự án mới thất bại");
			ra.addFlashAttribute("project", project);
			return "redirect:/admin/project/create";
			
		}
		pRepository.save(project);
		ra.addFlashAttribute("success", "Tạo dự án mới thành công" );
		return "redirect:/admin/contact";
	}
	
	@PostMapping("/update/{id}")
	public String updateProject(@PathVariable("id") Integer id) {
		return "";
	}
	
	

	
	
	
}
