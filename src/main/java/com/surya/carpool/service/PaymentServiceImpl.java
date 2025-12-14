package com.surya.carpool.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surya.carpool.model.PaymentReceipt;
import com.surya.carpool.repository.PaymentReceiptRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentReceiptRepository paymentReceiptRepository;

    @Autowired
    private CarService carService;

    @Override
    public void processPayment(Long carId, Double amount, Long userId) {

        // 1. Save payment
        PaymentReceipt receipt = new PaymentReceipt();
        receipt.setCarId(carId);
        receipt.setUserId(userId);
        receipt.setAmount(amount);
        receipt.setPaymentStatus("SUCCESS");
        receipt.setPaymentDate(LocalDateTime.now());

        paymentReceiptRepository.save(receipt);

        // 2. Mark car as BOOKED
        carService.markCarAsBooked(carId);
    }
}
