package com.surya.carpool.payment.service;

import com.surya.carpool.payment.model.Payment;

public interface PaymentService {

	void processPayment(Long carId, Double amount, Long userId);

	Payment initiatePayment(Payment payment);

	Payment confirmPayment(Long paymentId, boolean success);

}
