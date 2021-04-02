package com.thuanthanhtech.controllers.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/tuyen-dung")
public class RecruitmentClientController {
	
	
	@GetMapping
	public String recruitment(Model m) {
		List<String> breadcrumbs = new ArrayList<String>();
		breadcrumbs.add("Tuyển dụng");
		m.addAttribute("breadcrumbs", breadcrumbs);
		
		return "public-pages/recruitment";
	}
	
	@GetMapping("/co-hoi-nghe-nghiep")
	public String careerOpportUnities(Model m) {
		List<String> breadcrumbs = new ArrayList<String>();
		breadcrumbs.add("Tuyển dụng");
		breadcrumbs.add("Cơ hội nghề nghiệp");
		m.addAttribute("breadcrumbs", breadcrumbs);
		
		return "public-pages/careeropportunities";
	}
	
	@GetMapping("/quy-dinh-ho-so-ung-tuyen")
	public String specifiedCandidateProfile(Model m) {
		
		List<String> breadcrumbs = new ArrayList<String>();
		breadcrumbs.add("Tuyển dụng");
		breadcrumbs.add("Quy định hồ sơ ứng tuyển");
		m.addAttribute("breadcrumbs", breadcrumbs);
		
		return "public-pages/specifiedcandidateprofile";
	}
	
}
