package com.surya.carpool.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.surya.carpool.model.Payment;
import com.surya.carpool.model.PaymentMethod;
import com.surya.carpool.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentApiController {

	@Autowired
	private PaymentService paymentService;

	// STEP 1: Initiate Payment
	@PostMapping("/initiate")
	public ResponseEntity<?> initiate(@RequestBody Payment payment) {

		Payment saved = paymentService.initiatePayment(payment);

		Map<String, Object> response = new HashMap<>();
		response.put("paymentId", saved.getId());
		response.put("status", saved.getStatus());

		// ✅ Correct comparison: String vs Enum
		if (PaymentMethod.UPI.name().equals(saved.getPaymentMethod())) {
			response.put("upiQr", "upi://pay?pa=8121767185@axl&pn=SuryaCarPool&am=" + saved.getAmount());
		}

		return ResponseEntity.ok(response);
	}

	// STEP 2: Confirm Payment
	@PostMapping("/verify")
	public ResponseEntity<?> verify(@RequestParam Long paymentId, @RequestParam boolean success,
			@RequestParam(required = false) Long userId // ✅ SAFE: optional
	) {

		// userId is intentionally NOT required yet
		// Service layer handles fallback safely
		Payment payment = paymentService.confirmPayment(paymentId, success);

		return ResponseEntity.ok(payment);
	}
}
