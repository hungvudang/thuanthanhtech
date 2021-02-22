package com.thuanthanhtech.controllers.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeClientController {

	@GetMapping({"/home", "/"})
	public String home() {
		return "public-pages/index";
	}
}
