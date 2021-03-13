package com.thuanthanhtech.controllers.client;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuanthanhtech.entities.Category;
import com.thuanthanhtech.entities.Contact;
import com.thuanthanhtech.entities.ContactHelper;
import com.thuanthanhtech.entities.Helper;
import com.thuanthanhtech.repositories.CategoryRepository;
import com.thuanthanhtech.repositories.ContactRepository;

@Controller
@RequestMapping("/lien-he")
public class ContactClientController {

	@Autowired
	private ContactRepository ctRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private CategoryRepository cRepository;

	@GetMapping
	public String contact(Model m) {
		Contact contact = new Contact();
		contact.setStatus(0);
		
		List<String> targetBreadcrumbs = new ArrayList<String>();
		targetBreadcrumbs.add("Liên hệ");
		
		Category cate = cRepository.findBySlug("lien-he").get();
		
		Helper.getBreadcrumb(cate, cRepository, targetBreadcrumbs);
		
		m.addAttribute("breadcrumbs", targetBreadcrumbs);
		m.addAttribute("contact", contact);
		return "public-pages/contact";
	}

	@PostMapping("/save")
	public String saveContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult br,
			RedirectAttributes ra) {
		

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

			ra.addFlashAttribute("error", "Thông tin bạn cung cấp không đúng. Vui lòng thử lại");
			contact.setStatus(0);
			ra.addFlashAttribute("contact", contact);
			return "redirect:/lien-he";
		}
//		else
		
		// Kiểm tra email đã có trong csdl chưa
		Optional<Contact> opContact = ctRepository.findByEmail(contact.getEmail());
		if (opContact.isPresent()) {
			Contact nContact = opContact.get();
			nContact.setName(contact.getName());
			nContact.setPhone(contact.getPhone());
			nContact.setContent(contact.getContent());
			nContact.setEmail(contact.getEmail());
			nContact.setAddress(contact.getAddress());
			nContact.setStatus(0);
			
			ctRepository.saveAndFlush(nContact);
		} else {
			contact.setStatus(0);
			ctRepository.saveAndFlush(contact);
		}
		
		Thread sendMailTask = new Thread(()->{
			ContactHelper.sendMail(contact.getEmail(), javaMailSender);
		});
		sendMailTask.start();
		
		ra.addFlashAttribute("success",
				"Chúng tôi đã nhận được phản hồi của bạn. Chúng tôi sẽ liên hệ lại với bạn trong thời gian sớm nhất");
		return "redirect:/lien-he";
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "/errors/500";
	}
}
