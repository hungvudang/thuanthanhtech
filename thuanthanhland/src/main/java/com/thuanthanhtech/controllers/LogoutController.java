package com.thuanthanhtech.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest res, HttpServletResponse resp) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();  
        if (auth != null){
        	System.out.println(auth.getName());
           new SecurityContextLogoutHandler().logout(res, resp, auth);  
        }
         return "redirect:/login?logout";  
	}
}
