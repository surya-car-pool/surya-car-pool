package com.surya.carpool.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.surya.carpool.admin.service.AdminService;
import com.surya.carpool.booking.model.Booking;
import com.surya.carpool.car.model.Car;
import com.surya.carpool.user.model.User;

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

	@GetMapping("/users")
	@ResponseBody
	public List<User> getAllUsers() {
		return adminService.getAllUsers();
	}

	@GetMapping("/bookings")
	@ResponseBody
	public List<Booking> getAllBookings() {
		return adminService.getAllBookings();
	}
}
