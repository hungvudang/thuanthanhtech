package com.thuanthanhtech.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	@GetMapping
	public String index() {
		return "public-pages/index";
	}
	@GetMapping("/about.html")
	public String about() {
		return "public-pages/about";
	}
}
