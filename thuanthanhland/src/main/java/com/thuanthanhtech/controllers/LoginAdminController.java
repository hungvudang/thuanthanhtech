package com.thuanthanhtech.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//@PreAuthorize("#request.getRemoteAddr().equals(#request.getLocalAddr())")
public class LoginAdminController {
	@GetMapping("/login")
	public String login(HttpServletRequest request) {
		return "/admin-pages/login";
	}

}
