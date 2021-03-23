package com.thuanthanhtech.controllers.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ve-chung-toi")
public class AboutClientController {
	
	@GetMapping
	public String about(Model m) {
		return "/public-pages/about";
	}
}
