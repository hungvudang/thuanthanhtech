package com.thuanthanhtech.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuanthanhtech.entities.Helper;
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
		project.setPub(1);
		project.setHot(0);
		project.setImage(Helper.NO_IMAGE_MEDIUM_PNG);
		
		if (m.getAttribute("project") == null) {
			m.addAttribute("project", project);
		}
		
		m.addAttribute("active-project", true);
		return "admin-pages/project-create";
	}

	@PostMapping("/save")
	public String saveProject(@ModelAttribute("project") Project project, RedirectAttributes ra ) {
		if(project.getName()== null || project.getName().isBlank() || project.getName().isEmpty()  ) {
			
			
			ra.addFlashAttribute("error", "Tạo dự án mới thất bại");
			project.setImage(Helper.NO_IMAGE_MEDIUM_PNG);
			ra.addFlashAttribute("project", project);
			return "redirect:/admin/project/create";			
		}
		pRepository.save(project);
		ra.addFlashAttribute("success", "Tạo dự án mới thành công" );
		return "redirect:/admin/project";
	}
	
	@GetMapping("/detail/{id}")
	public String detailProject(@PathVariable("id") Integer id, Model m) {
		m.addAttribute("active_project", true);
		Optional<Project> opProject = pRepository.findById(id);
		if(opProject.isPresent()) {
			Project project =opProject.get();
			m.addAttribute("project", project);
			project.setImage(Helper.NO_IMAGE_MEDIUM_PNG);
			return "/admin-pages/project-detail";
		}
		return "redirect:/admin/project";
	}
	@PostMapping("update/{id}")
	public String updateProject(@PathVariable("id") Integer id, @ModelAttribute("project") Project project, RedirectAttributes ra) {
		if(project.getName().isBlank() || project.getName().isEmpty()) {
			ra.addFlashAttribute("error", "Cập nhật liên hệ thất bại");
			ra.addFlashAttribute("project", project);
			return "redirect:/admin/project/detail/"+id;
		}
		Optional<Project> opProject = pRepository.findById(id);
		if(opProject.isPresent()) {
			Project nProject =opProject.get();
			nProject.setId(project.getId());
			nProject.setName(project.getName());
			nProject.setTitle(project.getTitle());
			nProject.setContent(project.getContent());
			nProject.setDescription(project.getDescription());
			nProject.setImage(project.getImage());
			
			
			pRepository.save(nProject);
			ra.addFlashAttribute("success", "Cập nhật dự án thành công");
			return "redirect:/admin/project/detail/"+nProject.getId();
		}else {
			ra.addFlashAttribute("error", "Dự án không tồn tại");
		}
		return "redirect:/admin/project";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteProject(@PathVariable("id") Integer id, RedirectAttributes ra) {
		Optional<Project> opProject = pRepository.findById(id);
		if(opProject.isPresent()) {
			pRepository.deleteById(id);
			ra.addFlashAttribute("success", "Xóa dự án thành công");
		}else {
			ra.addFlashAttribute("error", "Xóa dự án thất bại");			
		}
		return "redirect:/admin/project";
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "admin-pages/500";
	}
	
	
}
