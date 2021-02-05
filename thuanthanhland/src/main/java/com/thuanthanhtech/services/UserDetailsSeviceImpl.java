package com.thuanthanhtech.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.thuanthanhtech.entities.User;
import com.thuanthanhtech.entities.UserDetailsImpl;
import com.thuanthanhtech.repositories.UserRepository;

@Service
public class UserDetailsSeviceImpl implements UserDetailsService{

	@Autowired
	private UserRepository uRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> opUser = uRepository.findByEmail(username);
		if (opUser.isPresent()) {
			return new UserDetailsImpl(opUser.get());
		} else {
			throw new UsernameNotFoundException("Email hoặc mật khẩu không đúng. Vui lòng kiểm tra lại");
		}
	}

}
