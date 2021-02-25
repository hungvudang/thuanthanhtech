package com.thuanthanhtech.controllers.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/recruitment")
public class RecruitmentClientController {
	@GetMapping
	public String recruitment(Model m) {
		return "public-pages/recruitment";
	}
	
	@GetMapping("/HRrecruitment")
	public String HRrecruitment(Model m) {
		return "public-pages/HRrecruitment";
	}
	
}
