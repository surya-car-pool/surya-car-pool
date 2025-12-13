package com.surya.carpool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.surya.carpool.service.PaymentService;

@Controller
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@PostMapping("/confirm-payment")
	public String confirmPayment(@RequestParam("bookingId") Long bookingId, RedirectAttributes redirectAttributes) {

		paymentService.confirmPayment(bookingId);

		redirectAttributes.addFlashAttribute("successMessage", "Payment successful!");

		return "redirect:/payment-success";
	}

	@GetMapping("/payment-success")
	public String paymentSuccess() {
		return "payment-success";
	}
}
