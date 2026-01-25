package com.surya.carpool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

	// Admin login page
	@GetMapping("/login")
	public String adminLogin() {
		return "admin/login";
	}

	// Admin dashboard
	@GetMapping("/dashboard")
	public String dashboard() {
		return "admin/dashboard";
	}

	// Manage cars page
	@GetMapping("/cars")
	public String cars() {
		return "admin/cars";
	}

	// Manage users page
	@GetMapping("/users")
	public String users() {
		return "admin/users";
	}

	// Bookings page
	@GetMapping("/bookings")
	public String bookings() {
		return "admin/bookings";
	}
}
