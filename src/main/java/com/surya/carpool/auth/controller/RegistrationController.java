package com.surya.carpool.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.surya.carpool.auth.dto.RegisterRequest;
import com.surya.carpool.user.model.Role;
import com.surya.carpool.user.model.User;
import com.surya.carpool.user.repository.UserRepository;

@Controller
public class RegistrationController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/register")
	public String register(@ModelAttribute RegisterRequest request) {

		// ===== Validate Email =====
		if (userRepository.existsByEmail(request.getRegEmail())) {
			return "redirect:/login?registerError=email";
		}

		// ===== Validate Phone =====
		if (userRepository.existsByPhone(request.getRegPhone())) {
			return "redirect:/login?registerError=phone";
		}

		// ===== Create User =====
		User user = new User();
		user.setName(request.getRegName());
		user.setEmail(request.getRegEmail());
		user.setPhone(request.getRegPhone());
		user.setPassword(passwordEncoder.encode(request.getRegPassword()));
		user.setRole(Role.USER);
		user.setEnabled(true);

		userRepository.save(user);

		return "redirect:/login?registered=true";
	}
}
