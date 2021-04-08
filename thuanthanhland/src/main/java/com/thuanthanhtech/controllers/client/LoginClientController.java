package com.thuanthanhtech.controllers.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thuanthanhtech.controllers.client.helper.CategoryClientHelper;

@Controller
@RequestMapping("/dang-nhap")
public class LoginClientController {
	
	@Autowired
	private CategoryClientHelper cateClientHelper;

	@GetMapping
	public String logginForm(Model m) {
		
		cateClientHelper.categories(m);
		return "/public-pages/client-login";
	}

}
