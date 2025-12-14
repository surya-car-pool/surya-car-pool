package com.surya.carpool.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.surya.carpool.bookings.Booking;
import com.surya.carpool.bookings.BookingService;
import com.surya.carpool.model.BookingForm;
import com.surya.carpool.model.Car;
import com.surya.carpool.service.CarService;

@Controller
@SessionAttributes("currentBooking") // keep booking data between pages
public class BookingPageController<PaymentService> {
	private final CarService carService; // service to fetch car details
	@Autowired
	BookingService bookingService;

	public BookingPageController(CarService carService) {
		this.carService = carService;
	}

	@ModelAttribute("currentBooking")
	public BookingForm currentBooking() {
		return new BookingForm();
	}

	/**
	 * Called when user clicks "Confirm Booking" on bookings.html -> stores form in
	 * session and redirects to payment page.
	 */
	@GetMapping("/bookings/ui")
	public String bookingsUi(@RequestParam(value = "carId", required = false) Long carId, Model model) {

		// Load list of cars for the page (if needed)
		model.addAttribute("cars", carService.findActiveCars());

		// Prepare booking form and pre-fill carId if present
		BookingForm bookingForm = new BookingForm();
		if (carId != null) {
			bookingForm.setCarId(carId);
			// Optional: load the selected car details (for display)
			Car selected = carService.findById(carId);
			if (selected != null) {
				model.addAttribute("selectedCar", selected);
			}
		}

		model.addAttribute("bookingForm", bookingForm);

		return "bookings"; // ensure this matches src/main/resources/templates/bookings.html
	}

	@PostMapping("/bookings")
	public String handleBookingSubmit(BookingForm form, @ModelAttribute("currentBooking") BookingForm sessionBooking,
			RedirectAttributes redirectAttributes) {

		// copy submitted fields into session booking
		sessionBooking.setCarId(form.getCarId());
		sessionBooking.setPickupLocation(form.getPickupLocation());
		sessionBooking.setPickupDateTime(form.getPickupDateTime());
		sessionBooking.setDropDateTime(form.getDropDateTime());

		sessionBooking.setCustomerName(form.getCustomerName());
		sessionBooking.setEmail(form.getEmail());
		sessionBooking.setPhone(form.getPhone());
		sessionBooking.setCustomerAddress(form.getCustomerAddress());
		sessionBooking.setNotes(form.getNotes());

		sessionBooking.setDrivingLicenseNumber(form.getDrivingLicenseNumber());
		sessionBooking.setDrivingLicenseExpiry(form.getDrivingLicenseExpiry());
		sessionBooking.setDrivingLicenseState(form.getDrivingLicenseState());
		sessionBooking.setAadharNumber(form.getAadharNumber());
		sessionBooking.setFixedDepositAmount(form.getFixedDepositAmount());

		sessionBooking.setPaymentMethod(form.getPaymentMethod());
		sessionBooking.setAmount(form.getAmount());

		// 2. Map BookingForm → Booking Entity
		Booking booking = new Booking();
		booking.setCarId(sessionBooking.getCarId());
		booking.setPickupLocation(sessionBooking.getPickupLocation());
		booking.setPickupDateTime(sessionBooking.getPickupDateTime());
		booking.setDropDateTime(sessionBooking.getDropDateTime());

		booking.setCustomerName(sessionBooking.getCustomerName());
		booking.setEmail(sessionBooking.getEmail());
		booking.setPhone(sessionBooking.getPhone());
		booking.setCustomerAddress(sessionBooking.getCustomerAddress());
		booking.setNotes(sessionBooking.getNotes());
		booking.setPaymentMethod(sessionBooking.getPaymentMethod());

		booking.setCreatedAt(LocalDateTime.now());
		booking.setStatus("PENDING_PAYMENT");

		// 3. Save to database
		Booking savedBooking = bookingService.createBookingFromEntity(booking);

		// 4. Store DB booking ID in session (used later in receipt)
		sessionBooking.setId(savedBooking.getId());
		// here you could also save to DB using your existing Booking entity
		redirectAttributes.addAttribute("bookingId", booking.getId());
		// redirect to payment page
		return "redirect:/payments/ui";
	}

	/**
	 * Payment page that shows booking summary and asks for payment confirmation.
	 */
	@GetMapping("/payments/ui")
	public String showPaymentPage(@ModelAttribute("currentBooking") BookingForm booking, Model model) {

		if (booking.getCustomerName() == null) {
			// if user opens /payments/ui directly without booking
			return "redirect:/bookings/ui";
		}

		model.addAttribute("booking", booking);
		return "payments"; // payments.html
	}

	@GetMapping("/view-bookings")
	public String viewBookingsPage() {
		return "view-bookings";
	}

}
