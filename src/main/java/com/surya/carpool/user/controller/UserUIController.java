package com.surya.carpool.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserUIController {

	@GetMapping("/ui")
	public String usersPage() {
		return "users"; // users.html in templates folder
	}
}
