package com.surya.carpool.booking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

		// ================================
		// 🔐 Fetch logged-in user details
		// ================================
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
}
