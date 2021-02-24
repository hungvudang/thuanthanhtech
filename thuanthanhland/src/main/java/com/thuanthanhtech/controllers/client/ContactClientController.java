package com.thuanthanhtech.controllers.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contact")
public class ContactClientController {
	@GetMapping
	public String contact(Model m) {
		return "public-pages/contact";
	}
}
