package com.thuanthanhtech.controllers.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MenuHeaderClientController {

	@Autowired
	private AboutClientController aboutClientController;
	
	@Autowired
	private ContactClientController contactClientController;
	
	@Autowired
	private HomeClientController homeClientController;
	
	@Autowired
	private NewsClientController newsClientController;
	
	@Autowired
	private ProjectClientController projectClientController;
	
	@Autowired
	private RecruitmentClientController recruitmentClientController;
	
	
	public void DispatcherRequest () {
		
	}
	
}
