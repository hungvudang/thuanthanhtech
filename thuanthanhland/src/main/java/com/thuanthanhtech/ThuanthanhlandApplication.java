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
		User admin = new User(null, "Adminstrator", "admin3t@thuanhthanhtech.com", 1, "Ha Noi", passwordEncoder.encode("admin@admin"), "0918273645", null, null, null);
		Optional<User> opAdmin = uRepository.findByEmail(admin.getEmail());
		if (!opAdmin.isPresent()) {
			uRepository.save(admin);
		}
	}
}
