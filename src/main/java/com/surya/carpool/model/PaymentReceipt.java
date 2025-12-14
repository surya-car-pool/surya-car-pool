package com.surya.carpool.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_receipt")
public class PaymentReceipt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// EXACT MATCH with service: setCarId(carId)
	@Column(name = "car_id", nullable = false)
	private Long carId;

	// EXACT MATCH with service: setUserId(userId)
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Double amount;

	@Column(name = "payment_status", nullable = false)
	private String paymentStatus;

	@Column(name = "payment_date", nullable = false)
	private LocalDateTime paymentDate;

	// ===== Getters & Setters =====

	public Long getId() {
		return id;
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}
}
