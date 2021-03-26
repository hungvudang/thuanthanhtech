package com.thuanthanhtech.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/errors")
public class CommonErrorController implements ErrorController{
	

	
	@GetMapping
	
	public String handleError(HttpServletRequest request, HttpServletResponse response) {
	    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	    
	    if (status != null) {
	        Integer statusCode = Integer.valueOf(status.toString());
	    
	        if(statusCode == HttpStatus.NOT_FOUND.value()) {
	            return "/errors/404";
	        }
	        else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
	            return "/errors/500";
	        }
	    }
	    return "/errors/error";
	}
	@GetMapping("/403")
	public String accessDenied(HttpServletRequest request, HttpServletResponse response) {
		
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		
//		if (auth != null) {
//			
//			new SecurityContextLogoutHandler().logout(request, response, auth);
//		}
		return "/errors/403";
	}

	@Override
	public String getErrorPath() {
		return "/errors";
	}
}
