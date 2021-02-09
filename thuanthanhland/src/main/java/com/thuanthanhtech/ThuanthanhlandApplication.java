package com.thuanthanhtech;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.thuanthanhtech.entities.User;
import com.thuanthanhtech.repositories.UserRepository;

@SpringBootApplication
public class ThuanthanhlandApplication implements CommandLineRunner {

	
	@Autowired
	private UserRepository uRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(ThuanthanhlandApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Optional<User> opAdmin = uRepository.findById(1);
		if (opAdmin.isPresent()) {
			User admin = opAdmin.get();
			admin.setEmail("admin3t@thuanhthanhtech.com");
			admin.setName("Adminstrator");
			admin.setAddress("Ha Noi");
			admin.setPassword(passwordEncoder.encode("admin@admin"));
			admin.setRole(1);
			admin.setPhone("0918273645");
			uRepository.save(admin);
		} else {
			User admin = new User();
			admin.setEmail("admin3t@thuanhthanhtech.com");
			admin.setName("Adminstrator");
			admin.setAddress("Ha Noi");
			admin.setPassword(passwordEncoder.encode("admin@admin"));
			admin.setRole(1);
			admin.setPhone("0918273645");
			uRepository.save(admin);
		}
	}
}
