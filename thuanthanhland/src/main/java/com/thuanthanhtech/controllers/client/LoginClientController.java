package com.thuanthanhtech.controllers.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.thuanthanhtech.entities.User;


@Controller
public class LoginClientController {
	@Autowired
	public User _email;
	
	@GetMapping("/loginclient")
	public String login() {
		return "/public-pages/loginclient";
	} 
	
	@PostMapping("/checkLogin")
	public String checkLogin(@RequestParam("email") String email , @RequestParam("password") String password) {
		if(_email.getEmail().equals(email) && _email.getPassword().equals(password)) {
			System.out.println("login thanh công ");
			return "redirect:/trang-chu";
		}else {
			System.out.println("login thất bại");
		}
		return "/public-pages/loginclient";
	}
	
//	@GetMapping("/logout")
//	public String logout() {
//		return "public-pages/loginclient";
//	}
//	
}
