package com.surya.carpool.service;

import com.surya.carpool.model.Payment;

public interface PaymentService {

	void processPayment(Long carId, Double amount, Long userId);

	Payment initiatePayment(Payment payment);

	Payment confirmPayment(Long paymentId, boolean success);

}
