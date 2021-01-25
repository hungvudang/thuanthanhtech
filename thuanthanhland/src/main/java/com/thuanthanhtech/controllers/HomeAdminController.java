package com.thuanthanhtech.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeAdminController {
	
	@GetMapping({"/home", ""})
	public String index() {
		return "admin-pages/index";
	}
}
