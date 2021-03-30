package com.thuanthanhtech.controllers.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dang-nhap")
public class LoginClientController {

	@GetMapping
	public String logginForm() {
		return "/public-pages/client-login";
	}

}
