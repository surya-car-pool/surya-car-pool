package com.surya.carpool.bookings;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BookingController {

	private final BookingService bookingService;

	public BookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}
	// ---------- REST API ----------

	@GetMapping("/api/bookings")
	@ResponseBody
	public List<Booking> getBookingsApi() {
		return bookingService.getAll();
	}

	@PostMapping("/api/bookings")
	@ResponseBody
	public ResponseEntity<Booking> createBookingApi(@RequestBody Booking booking) {
		// For API clients that send JSON (without file upload)
		Booking saved = bookingService.createBookingFromEntity(booking);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}
}
