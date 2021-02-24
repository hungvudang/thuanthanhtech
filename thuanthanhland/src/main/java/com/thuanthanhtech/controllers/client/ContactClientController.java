package com.thuanthanhtech.controllers.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactClientController {
	@GetMapping("/contact")
	public String contact(Model m) {
		return "public-pages/contact";
	}
}
