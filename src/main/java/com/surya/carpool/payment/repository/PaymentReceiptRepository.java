package com.surya.carpool.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.surya.carpool.payment.model.PaymentReceipt;

@Repository
public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt, Long> {
}
