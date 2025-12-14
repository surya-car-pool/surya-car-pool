package com.surya.carpool.service;
public interface PaymentService {

    void processPayment(Long carId, Double amount, Long userId);
}
