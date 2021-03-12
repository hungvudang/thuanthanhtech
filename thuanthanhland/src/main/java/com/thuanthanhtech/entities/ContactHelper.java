package com.thuanthanhtech.entities;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class ContactHelper {

	//	Constant for JavaMail
	// Replace with your email here:
	public static final String EMAIL = "thuanthanhland2021@gmail.com";

	// Replace password!!
	public static final String PASSWORD = "thuanthanh2021";

	public static void sendMail(String email, JavaMailSender javaMailSender) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(email);
		message.setSubject("Thuận Thành Land");
		message.setText(
				"Cảm ơn bạn đã phản hồi về cho chúng tôi. Chúng tôi sẽ liên hệ lại với bạn trong thời gian sớm nhất.");
		javaMailSender.send(message);
	}

}
