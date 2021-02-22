package com.thuanthanhtech.controllers.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client/home")
public class HomeClientController {

	@GetMapping
	public String index() {
		return "public-pages/index";
	}
}
