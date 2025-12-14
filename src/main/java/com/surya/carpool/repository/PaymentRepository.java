package com.surya.carpool.repository;

import com.surya.carpool.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Get all payments made by a customer email
    List<Payment> findByEmail(String email);

    // Get all payments for a specific car
    List<Payment> findByCarId(Long carId);

    // Get latest payment (useful for receipt / history)
    Payment findTopByOrderByCreatedAtDesc();
}
