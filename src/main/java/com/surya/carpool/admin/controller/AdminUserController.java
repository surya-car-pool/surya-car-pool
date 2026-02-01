package com.surya.carpool.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.surya.carpool.admin.service.AdminUserService;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

	private final AdminUserService adminUserService;

	public AdminUserController(AdminUserService adminUserService) {
		this.adminUserService = adminUserService;
	}

	@GetMapping
	public String listUsers(Model model) {
		model.addAttribute("users", adminUserService.getAllUsers());
		return "admin/users";
	}

	@PostMapping("/block/{id}")
	public String blockUser(@PathVariable Long id) {
		adminUserService.blockUser(id);
		return "redirect:/admin/users";
	}

	@PostMapping("/unblock/{id}")
	public String unblockUser(@PathVariable Long id) {
		adminUserService.unblockUser(id);
		return "redirect:/admin/users";
	}
}
