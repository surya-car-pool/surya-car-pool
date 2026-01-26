package com.surya.carpool.payment.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.surya.carpool.booking.model.Booking;
import com.surya.carpool.booking.repository.BookingRepository;
import com.surya.carpool.car.service.CarService;
import com.surya.carpool.payment.model.Payment;
import com.surya.carpool.payment.repository.PaymentRepository;
import com.surya.carpool.payment.service.PaymentService;

@Controller
@RequestMapping("/payments")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private CarService carService;
	
	private final BookingRepository bookingRepository;

	public PaymentController(BookingRepository bookingRepository) {
	    this.bookingRepository = bookingRepository;
	}

	@PostMapping("/success")
	public ResponseEntity<String> paymentSuccess(@RequestParam Long carId, @RequestParam Double amount,
			@RequestParam Long userId) {

		paymentService.processPayment(carId, amount, userId);
		return ResponseEntity.ok("Payment successful. Car booked.");
	}

	@PostMapping("/confirm")
	public String confirmPayment(@RequestParam String customerName, @RequestParam String email,
			@RequestParam String phone, @RequestParam Long carId, @RequestParam String pickupLocation,
			@RequestParam(required = false) String pickupDateTime, @RequestParam Double amount,
			@RequestParam String paymentMethod, @RequestParam(required = false) String notes, Model model) {

		if (pickupLocation == null || pickupLocation.isBlank()) {
			throw new IllegalArgumentException("Pickup location cannot be empty");
		}

		Payment payment = new Payment();
		payment.setCustomerName(customerName);
		payment.setEmail(email);
		payment.setPhone(phone);
		payment.setCarId(carId);
		payment.setPickupLocation(pickupLocation);
		payment.setAmount(amount);
		payment.setPaymentMethod(paymentMethod);
		payment.setNotes(notes);

		paymentRepository.save(payment);

		// Mark car as BOOKED
		carService.markCarAsBooked(carId);

		model.addAttribute("payment", payment);
		return "payment-receipt";
	}

	@GetMapping("/ui")
	public String paymentUI(@RequestParam Long carId, @RequestParam Double amount, @RequestParam Long bookingId,
			Model model) {

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

		model.addAttribute("booking", booking);
		model.addAttribute("carId", carId);
		model.addAttribute("amount", amount);

		return "payments";
	}

}
