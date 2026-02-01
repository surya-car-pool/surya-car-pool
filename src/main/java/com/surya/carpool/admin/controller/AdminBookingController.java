package com.surya.carpool.admin.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.surya.carpool.booking.dto.BookingCarViewDTO;
import com.surya.carpool.booking.service.BookingService;

@Controller
@RequestMapping("/admin")
public class AdminBookingController {

	private final BookingService bookingService;

	public AdminBookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	// ==========================
	// ADMIN – VIEW ALL BOOKINGS
	// ==========================
	@GetMapping("/bookings/all")
	public String viewAllBookings(Model model) {

		List<BookingCarViewDTO> bookings =
				bookingService.getAllBookingCarDetails();

		model.addAttribute("bookings", bookings);
		return "adminbookings";
	}
}
