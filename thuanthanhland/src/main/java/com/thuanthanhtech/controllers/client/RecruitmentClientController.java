package com.thuanthanhtech.controllers.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thuanthanhtech.entities.Category;
import com.thuanthanhtech.entities.Helper;
import com.thuanthanhtech.repositories.CategoryRepository;

@Controller
@RequestMapping("/tuyen-dung")
public class RecruitmentClientController {
	
	@Autowired
	private CategoryRepository cRepository;
	
	@GetMapping
	public String recruitment(Model m) {
		List<String> targetBreadcrumbs = new ArrayList<String>();
		targetBreadcrumbs.add("Tuyển dụng");
		
		Category cate = cRepository.findBySlug("tuyen-dung").get();
		
		Helper.getBreadcrumb(cate, cRepository, targetBreadcrumbs);
		
		m.addAttribute("breadcrumbs", targetBreadcrumbs);
		return "public-pages/recruitment";
	}
	
	@GetMapping("/co-hoi-nghe-nghiep")
	public String careerOpportUnities(Model m) {
		
		List<String> targetBreadcrumbs = new ArrayList<String>();
		targetBreadcrumbs.add("Cơ hội nghề nghiệp");
		targetBreadcrumbs.add("Tuyển dụng");
		targetBreadcrumbs.add(null);
//		Category cate = cRepository.findBySlug("co-hoi-nghe-nghiep").get();
//		Helper.getBreadcrumb(cate, cRepository, targetBreadcrumbs);
		
		m.addAttribute("breadcrumbs", targetBreadcrumbs);
		
		return "public-pages/careeropportunities";
	}
	
	@GetMapping("/quy-dinh-ho-so-ung-tuyen")
	public String specifiedCandidateProfile(Model m) {
		
		List<String> targetBreadcrumbs = new ArrayList<String>();
		targetBreadcrumbs.add("Quy định hồ sơ ứng tuyển");
		targetBreadcrumbs.add("Tuyển dụng");
		targetBreadcrumbs.add(null);
//		Category cate = cRepository.findBySlug("quy-dinh-ho-so-ung-tuyen").get();
//		Helper.getBreadcrumb(cate, cRepository, targetBreadcrumbs);
		
		m.addAttribute("breadcrumbs", targetBreadcrumbs);
		
		return "public-pages/specifiedcandidateprofile";
	}
	
}
