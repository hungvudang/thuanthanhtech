package com.thuanthanhtech.controllers.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutClientController {
	@GetMapping("/about")
	public String about(Model m) {
		return "/public-pages/about";
	}
}
