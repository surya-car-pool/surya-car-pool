package com.surya.carpool.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.surya.carpool.admin.service.AdminOwnerService;

@Controller
@RequestMapping("/admin/owners")
public class AdminOwnerController {

	private final AdminOwnerService adminOwnerService;

	public AdminOwnerController(AdminOwnerService adminOwnerService) {
		this.adminOwnerService = adminOwnerService;
	}

	@GetMapping
	public String ownersPage(Model model) {
		model.addAttribute("owners", adminOwnerService.getAllOwners());
		return "admin/owners";
	}

	@PostMapping("/approve/{id}")
	public String approve(@PathVariable Long id) {
		adminOwnerService.approveOwner(id);
		return "redirect:/admin/owners";
	}

	@PostMapping("/reject/{id}")
	public String reject(@PathVariable Long id) {
		adminOwnerService.rejectOwner(id);
		return "redirect:/admin/owners";
	}
}
