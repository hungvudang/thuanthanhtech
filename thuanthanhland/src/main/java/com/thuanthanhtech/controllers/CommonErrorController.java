package com.thuanthanhtech.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CommonErrorController {
	
	@GetMapping("/403")
	public String accessDenied() {
		return "/errors/403";
	}
}
