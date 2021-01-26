package com.thuanthanhtech.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeAdminController {
	
	@GetMapping({"/home", ""})
	public String index(Model m) {
		m.addAttribute("active_home", true);
		return "admin-pages/index";
	}
}
