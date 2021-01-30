package com.thuanthanhtech.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thuanthanhtech.repositories.SlideRepository;

@Controller
@RequestMapping("/slide")
public class SlideAdminController {
	
	@Autowired
	private SlideRepository sRepository;

}
