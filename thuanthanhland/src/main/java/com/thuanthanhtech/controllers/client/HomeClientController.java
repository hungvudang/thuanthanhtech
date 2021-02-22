package com.thuanthanhtech.controllers.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeClientController {

	@GetMapping
	public String home() {
		return "public-pages/index";
	}
}
