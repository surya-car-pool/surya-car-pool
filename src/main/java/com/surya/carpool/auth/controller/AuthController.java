package com.surya.carpool.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}

	@GetMapping("/admin/login")
	public String showAdminLoginPage() {
		return "admin/login";
	}
}
