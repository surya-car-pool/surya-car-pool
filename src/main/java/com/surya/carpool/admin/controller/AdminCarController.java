package com.surya.carpool.admin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.surya.carpool.admin.service.AdminCarService;

@Controller
@RequestMapping("/admin/cars")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCarController {

	private final AdminCarService adminCarService;

	public AdminCarController(AdminCarService adminCarService) {
		this.adminCarService = adminCarService;
	}

	@GetMapping
	public String manageCars(Model model) {
		model.addAttribute("cars", adminCarService.getAllCars());
		return "admin/managecars";
	}

	@PostMapping("/{id}/approve")
	public String approveCar(@PathVariable Long id) {
		adminCarService.approveCar(id);
		return "redirect:/admin/cars";
	}

	@PostMapping("/{id}/disable")
	public String disableCar(@PathVariable Long id) {
		adminCarService.disableCar(id);
		return "redirect:/admin/cars";
	}

	@PostMapping("/{id}/delete")
	public String deleteCar(@PathVariable Long id) {
		adminCarService.deleteCar(id);
		return "redirect:/admin/cars";
	}
}
