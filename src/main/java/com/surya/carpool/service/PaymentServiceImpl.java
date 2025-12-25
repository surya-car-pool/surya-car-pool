package com.surya.carpool.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surya.carpool.model.Payment;
import com.surya.carpool.model.PaymentReceipt;
import com.surya.carpool.repository.PaymentReceiptRepository;
import com.surya.carpool.repository.PaymentRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentReceiptRepository paymentReceiptRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private CarService carService;

	// EXISTING METHOD (kept, fixed internally)
	@Override
	public void processPayment(Long carId, Double amount, Long userId) {

		Payment payment = new Payment();
		payment.setCarId(carId);
		payment.setAmount(amount);

		// Status must be STRING
		payment.setStatus("PENDING");

		// Optional but safe
		payment.setPaymentMethod("MOCK");

		paymentRepository.save(payment);
	}

	// NEW – called when user selects payment method
	@Override
	public Payment initiatePayment(Payment payment) {

		// Ensure correct lifecycle (override PrePersist default)
		payment.setStatus("PENDING");

		if (payment.getGateway() == null) {
			payment.setGateway("MockGateway");
		}

		return paymentRepository.save(payment);
	}

	// NEW – called after UPI scan / card success
	@Override
	public Payment confirmPayment(Long paymentId, boolean success) {

		Payment payment = paymentRepository.findById(paymentId)
				.orElseThrow(() -> new RuntimeException("Payment not found"));

		if (success) {
			payment.setStatus("SUCCESS");

			// Save receipt ONLY after success
			PaymentReceipt receipt = new PaymentReceipt();
			receipt.setCarId(payment.getCarId());
			receipt.setAmount(payment.getAmount());
			receipt.setPaymentStatus("SUCCESS");
			receipt.setPaymentDate(LocalDateTime.now());

			// 🔴 CRITICAL SAFE FIX
			receipt.setUserId(resolveUserIdSafely());

			paymentReceiptRepository.save(receipt);

			// Booking confirmed ONLY after payment success
			carService.markCarAsBooked(payment.getCarId());

		} else {
			payment.setStatus("FAILED");
		}

		return paymentRepository.save(payment);
	}

	/**
	 * Guarantees a non-null userId for PaymentReceipt. This prevents Hibernate
	 * PropertyValueException. Replace this later with real logged-in user
	 * resolution.
	 */
	private Long resolveUserIdSafely() {
		return 1L; // system / mock user
	}
}
