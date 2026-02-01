package com.surya.carpool.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.surya.carpool.admin.service.AdminService;
import com.surya.carpool.car.model.Car;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	// ==========================
	// UI ENDPOINT (Dashboard)
	// ==========================
	@GetMapping("/dashboard")
	public String dashboard() {
		return "admin/dashboard"; // templates/admin/dashboard.html
	}

	// ==========================
	// REST API ENDPOINTS
	// ==========================

	@PostMapping("/cars")
	@ResponseBody
	public Car addCar(@RequestBody Car car) {
		return adminService.addCar(car);
	}

}
