package com.thuanthanhtech.controllers.client;

import java.io.IOException;
import java.sql.SQLException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.thuanthanhtech.entities.Contact;
import com.thuanthanhtech.repositories.ContactRepository;

@Controller
@RequestMapping("/lien-he")
public class ContactClientController {

	@Autowired
	private ContactRepository cRepository;

	@GetMapping
	public String contact(Model m) {
		Contact contact = new Contact();
		contact.setStatus(0);
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
		contact.setStatus(0);
		cRepository.saveAndFlush(contact);
		ra.addFlashAttribute("success",
				"Chúng tôi đã nhận được phản hồi của bạn. Chúng tôi sẽ liên hệ lại với bạn trong thời gian sớm nhất");
		return "redirect:/lien-he";
	}

	@ExceptionHandler(value = { Exception.class, IOException.class, SQLException.class })
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handlerException() {
		return "admin-pages/500";
	}
}
