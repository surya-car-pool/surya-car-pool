package com.surya.carpool.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.surya.carpool.model.Ride;
import com.surya.carpool.model.User;
import com.surya.carpool.repository.RideRepository;
import com.surya.carpool.repository.UserRepository;

@Controller
public class WebController {

	private final RideRepository rideRepo;
	private final UserRepository userRepo;

	public WebController(RideRepository rideRepo, UserRepository userRepo) {
		this.rideRepo = rideRepo;
		this.userRepo = userRepo;
	}

	@GetMapping({ "/", "/home" })
	public String home(Model model) {
		List<Ride> rides = rideRepo.findAll();
		model.addAttribute("rides", rides);
		return "index";
	}

	@GetMapping("/users/ui")
	public String users(Model model) {
		List<User> users = userRepo.findAll();
		model.addAttribute("users", users);
		return "users";
	}

	@GetMapping("/rides/ui")
	public String rides(Model model) {
		List<Ride> rides = rideRepo.findAll();
		model.addAttribute("rides", rides);
		return "rides";
	}

	@GetMapping("/bookings/ui")
	public String bookings() {
		return "bookings";
	}

	@GetMapping("/aboutus/ui")
	public String aboutus() {
		return "aboutus";
	}

	@GetMapping("/contactus/ui")
	public String contactus() {
		return "contactus";
	}
	
}
