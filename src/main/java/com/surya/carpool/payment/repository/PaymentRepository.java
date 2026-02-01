package com.surya.carpool.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.surya.carpool.payment.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	List<Payment> findByEmail(String email);

	List<Payment> findByCarId(Long carId);

	Payment findTopByOrderByCreatedAtDesc();
}
