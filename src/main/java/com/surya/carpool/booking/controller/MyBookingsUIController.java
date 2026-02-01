package com.surya.carpool.booking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.surya.carpool.booking.model.Booking;
import com.surya.carpool.booking.service.BookingService;

@Controller
public class MyBookingsUIController {

	@Autowired
	private BookingService bookingService;

	@GetMapping("/mybookings/ui")
	public String myBookingsPage(Model model) {

		List<Booking> bookings = bookingService.getBookingsForCurrentUser();

		model.addAttribute("bookings", bookings);

		return "mybookings";
	}
}
