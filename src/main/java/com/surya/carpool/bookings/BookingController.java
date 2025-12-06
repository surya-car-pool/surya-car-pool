package com.surya.carpool.bookings;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.surya.carpool.model.BookingForm;

@Controller
public class BookingController {

	private final BookingService bookingService;

	public BookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	// ---------- UI ----------

	@GetMapping("/bookings/ui")
	public String bookingsPage(@RequestParam(value = "carId", required = false) Long carId,
			@RequestParam(value = "success", required = false) String success, Model model) {

		BookingForm form = new BookingForm();
		form.setCarId(carId);

		model.addAttribute("bookingForm", form);
		model.addAttribute("carId", carId);
		model.addAttribute("success", success);

		return "bookings"; // bookings.html
	}

	@PostMapping("/bookings")
	public String createBooking(@ModelAttribute BookingForm bookingForm, RedirectAttributes redirectAttributes) {

		try {
			bookingService.createBooking(bookingForm);
			redirectAttributes.addAttribute("success", "true");
		} catch (IOException e) {
			redirectAttributes.addAttribute("success", "false");
		}

		// Redirect back to UI
		return "redirect:/bookings/ui";
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
