package com.surya.carpool.booking.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.surya.carpool.booking.model.Booking;
import com.surya.carpool.booking.model.BookingForm;
import com.surya.carpool.booking.service.BookingService;
import com.surya.carpool.car.model.Car;
import com.surya.carpool.car.model.CarStatus;
import com.surya.carpool.car.repository.CarRepository;
import com.surya.carpool.user.model.User;

@Controller
@RequestMapping
public class BookingController {

	private final BookingService bookingService;
	private final CarRepository carRepository;

	public BookingController(BookingService bookingService, CarRepository carRepository) {
		this.bookingService = bookingService;
		this.carRepository = carRepository;
	}

	// ==========================
	// Book Car List Page
	// ==========================
	@GetMapping("/bookcar/ui")
	public String bookCarUI(Model model) {

		List<Car> cars = carRepository.findByActiveTrueAndOwnerEnabledTrueAndStatus(CarStatus.AVAILABLE);
		model.addAttribute("cars", cars);

		return "bookcar";
	}

	// ==========================
	// Booking Form Page
	// ==========================
	@GetMapping("/bookings/ui")
	public String bookingFormUI(@RequestParam Long carId, Model model) {

		Car car = carRepository.findById(carId)
				.orElseThrow(() -> new RuntimeException("Car not found with id " + carId));

		BookingForm bookingForm = new BookingForm();
		bookingForm.setCarId(carId);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication != null && authentication.isAuthenticated()
				&& !"anonymousUser".equals(authentication.getPrincipal())) {

			User loggedInUser = (User) authentication.getPrincipal();

			bookingForm.setCustomerName(loggedInUser.getName());
			bookingForm.setEmail(loggedInUser.getEmail());
			bookingForm.setPhone(loggedInUser.getPhone());
		}

		model.addAttribute("bookingForm", bookingForm);
		model.addAttribute("car", car);

		return "bookings";
	}

	// ==========================
	// Submit Booking Form
	// ==========================
	@PostMapping("/bookings")
	public String submitBookingForm(@ModelAttribute BookingForm bookingForm, RedirectAttributes redirectAttributes) {

		Booking savedBooking = bookingService.createBookingFromForm(bookingForm);

		return "redirect:/payments/ui?carId=" + savedBooking.getCar().getId() + "&amount=" + savedBooking.getAmount()
				+ "&bookingId=" + savedBooking.getId();
	}

	// ==========================
	// REST APIs
	// ==========================
	@GetMapping("/api/bookings")
	@ResponseBody
	public List<Booking> getBookingsApi() {
		return bookingService.getAll();
	}

	@PostMapping("/api/bookings")
	@ResponseBody
	public ResponseEntity<Booking> createBookingApi(@RequestBody Booking booking) {
		Booking saved = bookingService.createBookingFromEntity(booking);
		return new ResponseEntity<>(saved, HttpStatus.CREATED);
	}

	// ==================================================
	// NEW: MODAL BUTTON ACTION ENDPOINTS
	// ==================================================

	// CANCEL
	@PostMapping("/bookings/cancel/{id}")
	public String cancelBooking(@PathVariable Long id, RedirectAttributes ra) {

		try {
			bookingService.cancelBooking(id);
			ra.addFlashAttribute("cancelSuccess", true);
			ra.addFlashAttribute("successMessage", "Your booking was cancelled successfully.");
		} catch (RuntimeException ex) {
			ra.addFlashAttribute("cancelError", true);
			ra.addFlashAttribute("errorMessage", ex.getMessage());
		}

		return "redirect:/mybookings/ui";
	}

	// EXTEND PAGE
	@GetMapping("/bookings/extend/{id}")
	public String extendBookingPage(@PathVariable Long id, Model model) {
		model.addAttribute("bookingId", id);
		return "extend-booking";
	}

	// EXTEND SUBMIT
	@PostMapping("/bookings/extend")
	public String extendBooking(@RequestParam Long bookingId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newDropDate) {

		bookingService.extendBooking(bookingId, newDropDate);
		return "redirect:/mybookings";
	}

	// RESCHEDULE PAGE
	@GetMapping("/bookings/reschedule/{id}")
	public String reschedulePage(@PathVariable Long id, Model model) {
		model.addAttribute("bookingId", id);
		return "reschedule-booking";
	}

	// RESCHEDULE SUBMIT
	@PostMapping("/bookings/reschedule")
	public String rescheduleBooking(@RequestParam Long bookingId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime pickup,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime drop) {

		bookingService.rescheduleBooking(bookingId, pickup, drop);
		return "redirect:/mybookings";
	}

	// INVOICE (dummy PDF for now)
	@GetMapping("/bookings/invoice/{id}")
	public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long id) {

		byte[] dummyPdf = "Invoice will be available soon".getBytes();

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + id + ".pdf")
				.contentType(MediaType.APPLICATION_PDF).body(dummyPdf);
	}
}
