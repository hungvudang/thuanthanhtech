package com.thuanthanhtech.controllers.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tuyen-dung")
public class RecruitmentClientController {
	
	
	@GetMapping
	public String recruitment(Model m) {
		return "public-pages/recruitment";
	}
	
	@GetMapping("/co-hoi-nghe-nghiep")
	public String careerOpportUnities(Model m) {
		
		return "public-pages/careeropportunities";
	}
	
	@GetMapping("/quy-dinh-ho-so-ung-tuyen")
	public String specifiedCandidateProfile(Model m) {
		
		return "public-pages/specifiedcandidateprofile";
	}
	
}
