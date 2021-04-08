package com.thuanthanhtech.controllers.client;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thuanthanhtech.entities.User;
import com.thuanthanhtech.repositories.CategoryRepository;
import com.thuanthanhtech.repositories.UserRepository;

@Controller
public class RegisterClientController {

	@SuppressWarnings("unused")
	@Autowired
	private UserRepository uRepository;

	@SuppressWarnings("unused")
	@Autowired
	private CategoryRepository cRepository;


	@GetMapping("/register")
	public String user(Model m) {
		User user = new User();
		user.setRole(1);
		
		
		m.addAttribute("user", user);
		return "public-pages/register";
	}
	
	@PostMapping("/register/save")
	public String saveUser(@Valid @ModelAttribute("User") User user, BindingResult br, 
			RedirectAttributes ra) {
		// validate dữ liệu
		
		//Kiểm tra email đã có trong csdl chưa
		
		Optional<User> opUser = uRepository.findByEmail(user.getEmail());
		if(opUser.isPresent()) {
			User nUser = opUser.get();
			nUser.setName(user.getName());
			nUser.setEmail(user.getEmail());
			nUser.setAddress(user.getAddress());
			nUser.setPassword(user.getPassword());
			
			uRepository.saveAndFlush(nUser);
			
		}else {
			user.setRole(0);
			uRepository.saveAndFlush(user);
		}
		return "redirect:/register";
	}
}
