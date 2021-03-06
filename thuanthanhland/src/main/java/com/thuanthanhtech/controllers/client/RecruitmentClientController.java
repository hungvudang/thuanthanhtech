package com.thuanthanhtech.controllers.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thuanthanhtech.controllers.client.helper.CategoryClientHelper;
import com.thuanthanhtech.controllers.client.utils.Filterable;
import com.thuanthanhtech.entities.Category;
import com.thuanthanhtech.entities.Recruitment;
import com.thuanthanhtech.entities.RecruitmentHelper;
import com.thuanthanhtech.repositories.CategoryRepository;
import com.thuanthanhtech.repositories.RecruitmentRepository;

@Controller
@RequestMapping("/tuyen-dung")
public class RecruitmentClientController implements Filterable{
	
	@Autowired
	private RecruitmentRepository rRepository;
	
	@Autowired
	private CategoryRepository cRepository;
	
	@Autowired
	private CategoryClientHelper cateClientHelper;
	
	@GetMapping
	public String recruitment(Model m) {
		List<String> breadcrumbs = new ArrayList<String>();
		breadcrumbs.add("Tuyển dụng");
		breadcrumbs.add("Chính sách nhân sự");
		m.addAttribute("breadcrumbs", breadcrumbs);
		
		// menu header
		cateClientHelper.categories(m);
		
		//menu recruitment
		List<Category> childs = RecruitmentHelper.getChilds(cRepository, "tuyen-dung");
		m.addAttribute("childs", childs);
		
		return "public-pages/recruitment-hr-policies";
	}
	
	@GetMapping("/chinh-sach-nhan-su")
	public String hrPolicies(Model m) {
		List<String> breadcrumbs = new ArrayList<String>();
		breadcrumbs.add("Tuyển dụng");
		breadcrumbs.add("Chính sách nhân sự");
		m.addAttribute("breadcrumbs", breadcrumbs);
		m.addAttribute("active_hr_policies", true);
		
		cateClientHelper.categories(m);
		
		List<Category> childs = RecruitmentHelper.getChilds(cRepository, "tuyen-dung");
		m.addAttribute("childs", childs);
		
		return "public-pages/recruitment-hr-policies";
	}
	
	
	
	@GetMapping("/co-hoi-nghe-nghiep")
	public String careerOpportUnities(Model m) {
		List<String> breadcrumbs = new ArrayList<String>();
		breadcrumbs.add("Tuyển dụng");
		breadcrumbs.add("Cơ hội nghề nghiệp");
		m.addAttribute("breadcrumbs", breadcrumbs);
		
		List<Recruitment> recruitments = rRepository.findByPub(1);
		m.addAttribute("recruitments", recruitments);
		m.addAttribute("active_career_opportunities", true);
		
		cateClientHelper.categories(m);
		
		List<Category> childs = RecruitmentHelper.getChilds(cRepository, "tuyen-dung");
		m.addAttribute("childs", childs);
		
		return "public-pages/recruitment-career-opportunities";
	}
	
	@GetMapping("/quy-dinh-ho-so-ung-tuyen")
	public String specifiedCandidateProfile(Model m) {
		
		List<String> breadcrumbs = new ArrayList<String>();
		breadcrumbs.add("Tuyển dụng");
		breadcrumbs.add("Quy định hồ sơ ứng tuyển");
		m.addAttribute("breadcrumbs", breadcrumbs);
		m.addAttribute("active_specified_candidate_profile", true);
		
		cateClientHelper.categories(m);
		
		List<Category> childs = RecruitmentHelper.getChilds(cRepository, "tuyen-dung");
		m.addAttribute("childs", childs);
		
		return "public-pages/recruitment-specified-candidate-profile";
	}
	
	@GetMapping("/co-hoi-nghe-nghiep/{slug}")
	public String detail(@PathVariable("slug") String slug, Model m) {
		Optional<Recruitment> opRecruiment = rRepository.findBySlug(slug);
		if (opRecruiment.isPresent()) {
			Recruitment recruitment = opRecruiment.get();
			m.addAttribute("recruitment", recruitment);
			
			List<String> breadcrumbs = new ArrayList<String>();
			breadcrumbs.add("Tuyển dụng");
			breadcrumbs.add("Cơ hội nghề nghiệp");
			breadcrumbs.add(recruitment.getPosition());
			m.addAttribute("breadcrumbs", breadcrumbs);
			
			cateClientHelper.categories(m);
			
			List<Category> childs = RecruitmentHelper.getChilds(cRepository, "tuyen-dung");
			m.addAttribute("childs", childs);
			
			return "public-pages/recruitment-career-detail";
		}
		return "redirect:/tuyen-dung/co-hoi-nghe-nghiep";
	}
	
	
	
}
