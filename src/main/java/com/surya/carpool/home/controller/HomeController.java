package com.surya.carpool.home.controller;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.surya.carpool.booking.model.BookingStatus;
import com.surya.carpool.booking.repository.BookingRepository;
import com.surya.carpool.car.model.CarStatus;
import com.surya.carpool.car.repository.CarRepository;
import com.surya.carpool.user.model.ApprovalStatus;
import com.surya.carpool.user.model.Role;
import com.surya.carpool.user.repository.UserRepository;

@Controller
public class HomeController {

	private final UserRepository userRepository;
	private final CarRepository carRepository;
	private final BookingRepository bookingRepository;

	public HomeController(UserRepository userRepository, CarRepository carRepository,
			BookingRepository bookingRepository) {
		this.userRepository = userRepository;
		this.carRepository = carRepository;
		this.bookingRepository = bookingRepository;
	}

	@GetMapping({ "/", "/home" })
	public String home(Model model) {
		model.addAttribute("customerCount", userRepository.countByRoleAndEnabledTrue(Role.USER));
		model.addAttribute("ownerCount",
				userRepository.countByRoleAndApprovalStatusAndEnabledTrue(Role.OWNER, ApprovalStatus.APPROVED));
		model.addAttribute("availableCarCount",
				carRepository.countByActiveTrueAndOwnerEnabledTrueAndStatus(CarStatus.AVAILABLE));
		model.addAttribute("completedTripCount", bookingRepository.countByStatus(BookingStatus.COMPLETED));
		return "index"; // templates/index.html
	}
}
