package com.thuanthanhtech.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thuanthanhtech.entities.Contact;
import com.thuanthanhtech.entities.User;
import com.thuanthanhtech.repositories.ContactRepository;
import com.thuanthanhtech.repositories.NewsRepository;



@Controller
@RequestMapping("/contact")
public class ContactAdminController {
	@Autowired
	private ContactRepository ctRepository;
	@GetMapping
	public String contact (Model m) {
		List<Contact> contacts =ctRepository.findAll();
		m.addAttribute("contacts", contacts);
		m.addAttribute("active_contact", true);
		return "/admin-pages/contact";
	}
	@GetMapping("/create")
	public String createContact(Model m) {
		Contact contact = new Contact();
		contact.setId(0);
		m.addAttribute("contact", contact);
		m.addAttribute("active-contact", true);
		return "/admin-page/create-contact";
	}
	@PostMapping("/save")
	public String saveContact(@ModelAttribute("contact") Contact contact) {
		if(true) {
			
		}
		return "redirect:/contact/create";
	}
	
	
	
}
