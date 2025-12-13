package com.surya.carpool.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surya.carpool.bookings.Booking;
import com.surya.carpool.bookings.BookingRepository;
import com.surya.carpool.model.PaymentReceipt;

@Service
public class PaymentService {

	@Autowired
	private BookingRepository bookingRepository;

	public PaymentReceipt getReceiptByBookingId(Long bookingId) {

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		PaymentReceipt receipt = new PaymentReceipt();

		receipt.setReceiptNumber("RCPT-" + booking.getId());
		receipt.setPaymentDateTime(LocalDateTime.now());
		receipt.setStatus(booking.isPaymentConfirmed() ? "SUCCESS" : "PENDING");

		receipt.setAmountPaid(booking.getAmount());

		receipt.setCarId(booking.getCarId());
		// receipt.setCarModel(booking.getCarModel());

		receipt.setCustomerName(booking.getCustomerName());
		receipt.setCustomerEmail(booking.getEmail());
		receipt.setCustomerPhone(booking.getPhone());

		receipt.setPaymentMethod(booking.getPaymentMethod());
		receipt.setTransactionId("TXN-" + booking.getId());
		receipt.setGateway("MockGateway");

		return receipt;
	}

	public void confirmPayment(Long bookingId) {
		// TODO Auto-generated method stub

	}
}
