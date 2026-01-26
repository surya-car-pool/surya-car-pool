package com.surya.carpool.auth.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.surya.carpool.user.model.User;
import com.surya.carpool.user.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User login(String identifier, String password) {

		User user = null;

		// 1. Try email
		Optional<User> byEmail = userRepository.findByEmail(identifier);
		if (byEmail.isPresent()) {
			user = byEmail.get();
		} else {
			// 2. Try phone
			List<User> byPhone = userRepository.findByPhone(identifier);
			if (!byPhone.isEmpty()) {
				user = byPhone.get(0);
			}
		}

		if (user == null) {
			throw new RuntimeException("Invalid email/phone or password");
		}

		if (!user.isEnabled()) {
			throw new RuntimeException("User is disabled");
		}

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new RuntimeException("Invalid email/phone or password");
		}

		return user;
	}
}
