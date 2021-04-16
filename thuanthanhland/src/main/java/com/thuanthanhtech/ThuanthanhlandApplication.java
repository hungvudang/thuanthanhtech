package com.thuanthanhtech;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.thuanthanhtech.entities.Helper;
import com.thuanthanhtech.entities.User;
import com.thuanthanhtech.repositories.UserRepository;

@SpringBootApplication
public class ThuanthanhlandApplication extends SpringBootServletInitializer implements CommandLineRunner {

	private static String ADMIN_EMAIL = "admin@thuanthanh.com";
	private static String ADMIN_PASSWORD = "admin@admin";
	@Autowired
	private UserRepository uRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ThuanthanhlandApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ThuanthanhlandApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User admin = new User(null, "Adminstrator", ADMIN_EMAIL, 1, "Ha Noi", passwordEncoder.encode(ADMIN_PASSWORD),
				"0918273645", Helper.NO_IMAGE_MEDIUM_PNG, null, null);
		Optional<User> opAdmin = uRepository.findByEmail(admin.getEmail());
		if (!opAdmin.isPresent()) {
			uRepository.save(admin);
		}
	}
}
