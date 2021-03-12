package com.thuanthanhtech.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	@GetMapping("/login")
	public String login(@RequestParam(name = "denied", required = false) String denied) {
		if (denied != null) {
			return "redirect:/";
		}
		return "login";
	}

}
