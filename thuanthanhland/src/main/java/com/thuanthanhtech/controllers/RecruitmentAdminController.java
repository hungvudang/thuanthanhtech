package com.thuanthanhtech.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thuanthanhtech.entities.Recruitment;
import com.thuanthanhtech.repositories.RecruitmentRepository;

@Controller
@RequestMapping("/admin/recruitment")
public class RecruitmentAdminController {
	
	@Autowired
	private RecruitmentRepository rRepository;
	
	@GetMapping
	public String recruitments(Model m) {
		List<Recruitment> recruitments = rRepository.findAll();
		
		m.addAttribute("active_recruitment", true);
		m.addAttribute("recruitments", recruitments);
		
		return "/admin-pages/recruitment";
	}
	
	@GetMapping("/create")
	public String create(Model m) {
		if (!m.containsAttribute("recruitment")) {
			Recruitment recruitment = new Recruitment();
			m.addAttribute("recruitment", recruitment);
		}
		
		m.addAttribute("actiive_recruitment", true);
		return "/admin-pages/recruitment-create";
	}
	
	@PostMapping("/save")
	public String save() {
		return "redirect:/admin/recruitment/create";
	}
	
	@GetMapping("/detail/{id}")
	public String detail() {
		return "/admin-pages/recruitment-detail";
	}
	
	@PostMapping("/update/{id}")
	public String update() {
		return "redirect:/admin/recruitment/detail";
	}
	
	
	public String delete() {
		return "redirect:/admin/recruitment";
	}

}
