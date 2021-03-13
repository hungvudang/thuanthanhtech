package com.thuanthanhtech.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuanthanhtech.entities.Contact;
import com.thuanthanhtech.repositories.ContactRepository;

@Controller
@RequestMapping("/admin/contact")
public class ContactAdminController {
	@Autowired
	private ContactRepository ctRepository;

	@GetMapping
	public String contact(Model m) {
		List<Contact> contacts = ctRepository.findAll();
		m.addAttribute("contacts", contacts);
		m.addAttribute("active_contact", true);
		return "/admin-pages/contact";
	}

	@GetMapping("/create")
	public String createContact(Model m) {
		Contact contact = new Contact();
		contact.setStatus(0);
		
		if(m.getAttribute("contact") == null) {
			m.addAttribute("contact", contact);
		}
		
		m.addAttribute("active-contact", true);
		return "admin-pages/create-contact";
	}

	@PostMapping("/save")
	public String saveContact(@Valid @ModelAttribute("contact") Contact contact, RedirectAttributes ra, BindingResult br) {
		
		if (br.hasErrors()) {
			
			if (br.hasFieldErrors("name")) {
				ra.addFlashAttribute("isNameError", true);
				ra.addFlashAttribute("nameErrorMessage", br.getFieldError("name").getDefaultMessage());
			}

			if (br.hasFieldErrors("email")) {
				ra.addFlashAttribute("isEmailError", true);
				ra.addFlashAttribute("emailErrorMessage", br.getFieldError("email").getDefaultMessage());
			}

			if (br.hasFieldErrors("phone")) {
				ra.addFlashAttribute("isPhoneError", true);
				ra.addFlashAttribute("phoneErrorMessage", br.getFieldError("phone").getDefaultMessage());
			}
			
			ra.addFlashAttribute("error", "Tạo liên hệ mới thất bại");
			contact.setStatus(0);
			ra.addFlashAttribute("contact", contact);
			return "redirect:/admin/contact/create";
		}
//		else
		
		ctRepository.saveAndFlush(contact);
		ra.addFlashAttribute("success", "Tạo liên hệ mới thành công");
		return "redirect:/admin/contact";
	}

	@GetMapping("/detail/{id}")
	public String detailContact(@PathVariable("id") Integer id, Model m) {
		m.addAttribute("active_contact", true);
		Optional<Contact> opContact = ctRepository.findById(id);
		if (opContact.isPresent()) {
			Contact contact = opContact.get();
			m.addAttribute("contact", contact);
			return "/admin-pages/contact-detail";
		}
		return "redirect:/admin/contact";
	}

	@PostMapping("/update/{id}")
	public String updateContact(@PathVariable("id") Integer id, @Valid @ModelAttribute("contact") Contact contact,
			BindingResult br, RedirectAttributes ra) {
		
		if(br.hasErrors()) {
			
			if (br.hasFieldErrors("name")) {
				ra.addFlashAttribute("isNameError", true);
				ra.addFlashAttribute("nameErrorMessage", br.getFieldError("name").getDefaultMessage());
			}

			if (br.hasFieldErrors("email")) {
				ra.addFlashAttribute("isEmailError", true);
				ra.addFlashAttribute("emailErrorMessage", br.getFieldError("email").getDefaultMessage());
			}

			if (br.hasFieldErrors("phone")) {
				ra.addFlashAttribute("isPhoneError", true);
				ra.addFlashAttribute("phoneErrorMessage", br.getFieldError("phone").getDefaultMessage());
			}
			
			ra.addFlashAttribute("error", "Cập nhật liên hệ thất bại");
			return "redirect:/admin/contact/detail/" + id;
		}
		
		
		Optional<Contact> opContact = ctRepository.findById(id);
		if (opContact.isPresent()) {
			Contact nContact = opContact.get();
			nContact.setId(contact.getId());
			nContact.setName(contact.getName());
			nContact.setEmail(contact.getEmail());
			nContact.setAddress(contact.getAddress());
			nContact.setPhone(contact.getPhone());
			nContact.setContent(contact.getContent());
			
			ctRepository.save(nContact);
			ra.addFlashAttribute("success", "Cập nhật liên hệ thành công");
			return "redirect:/admin/contact/detail/" + nContact.getId();
		}
//		else

		ra.addFlashAttribute("error", "Liên hệ không tồn tại");
		return "redirect:/admin/contact";
	}

	@GetMapping("/delete/{id}")
	public String deleteContact(@PathVariable("id") Integer id, RedirectAttributes ra) {
		Optional<Contact> opContact = ctRepository.findById(id);
		if (opContact.isPresent()) {
			ctRepository.deleteById(id);
			ra.addFlashAttribute("success", "Liên hệ đã được xóa thành công");
		} else {
			ra.addFlashAttribute("error", "Liên hệ không tồn tại hoặc đã bị xóa");
		}
		return "redirect:/admin/contact";
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "/errors/500";
	}
}
