package com.thuanthanhtech.controllers.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ve-chung-toi")
public class AboutClientController {
	
	@GetMapping
	public String about(Model m) {
		List<String> breadcrumbs = new ArrayList<String>();
		breadcrumbs.add("Về chúng tôi");
		m.addAttribute("breadcrumbs", breadcrumbs);
		return "/public-pages/about";
	}
}
