package com.surya.carpool.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/*
	 * ====================== CUSTOMER DETAILS ======================
	 */

	@Column(nullable = false)
	private String customerName;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String phone;

	/*
	 * ====================== BOOKING DETAILS ======================
	 */

	@Column(nullable = false)
	private Long carId;

	@Column(nullable = true)
	private String pickupLocation;

	private LocalDateTime pickupDateTime;

	/*
	 * ====================== PAYMENT DETAILS ======================
	 */

	@Column(nullable = false)
	private Double amount;

	@Column(nullable = false)
	private String paymentMethod; // CARD / UPI / NETBANKING / CASH

	private String notes;

	/*
	 * ====================== SYSTEM / META ======================
	 */

	@Column(nullable = false)
	private String status; // SUCCESS / FAILED / PENDING

	@Column(nullable = false)
	private String gateway; // MockGateway, Razorpay, Stripe, etc.

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	/*
	 * ====================== LIFECYCLE CALLBACK ======================
	 */

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
		if (this.status == null) {
			this.status = "SUCCESS"; // default for mock payment
		}
		if (this.gateway == null) {
			this.gateway = "MockGateway";
		}
	}

	/*
	 * ====================== GETTERS & SETTERS ======================
	 */

	public Long getId() {
		return id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public String getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public LocalDateTime getPickupDateTime() {
		return pickupDateTime;
	}

	public void setPickupDateTime(LocalDateTime pickupDateTime) {
		this.pickupDateTime = pickupDateTime;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
