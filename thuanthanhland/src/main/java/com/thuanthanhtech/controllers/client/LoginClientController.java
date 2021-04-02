package com.thuanthanhtech.controllers.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dang-nhap")
public class LoginClientController {
	
	@Autowired
	private CategoryClientController cateClientController;

	@GetMapping
	public String logginForm(Model m) {
		
		cateClientController.categories(m);
		return "/public-pages/client-login";
	}

}
