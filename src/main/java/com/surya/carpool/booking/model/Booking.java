package com.surya.carpool.booking.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.surya.carpool.car.model.Car;
import com.surya.carpool.user.model.User;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings", indexes = { @Index(name = "idx_booking_car", columnList = "car_id"),
		@Index(name = "idx_booking_customer", columnList = "customer_id"),
		@Index(name = "idx_booking_status", columnList = "status") })
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// ========================
	// RELATIONSHIPS
	// ========================

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "car_id", nullable = false)
	private Car car;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "customer_id", nullable = true)
	private User customer;

	// ========================
	// SNAPSHOT CUSTOMER DATA
	// ========================

	@Column(length = 150)
	private String customerName;

	@Column(length = 150)
	private String email;

	@Column(length = 30)
	private String phone;

	// ========================
	// BOOKING STATUS
	// ========================

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private BookingStatus status = BookingStatus.ACTIVE;

	// ========================
	// CAR SNAPSHOT
	// ========================

	@Column(length = 150)
	private String carModel;

	// ========================
	// PAYMENT
	// ========================

	@Column(nullable = false)
	private boolean paymentConfirmed = false;

	@Column(length = 20)
	private String paymentMethod;

	@Column(precision = 10, scale = 2)
	private BigDecimal amount;

	// ========================
	// ADDRESS & LOCATION
	// ========================

	@Column(length = 500)
	private String customerAddress;

	@Column(length = 500)
	private String pickupLocation;

	@Column(length = 1000)
	private String notes;

	// ========================
	// DATES (real fields)
	// ========================

	@Column(nullable = false)
	private LocalDateTime pickupDateTime;

	@Column(nullable = false)
	private LocalDateTime dropDateTime;

	// ========================
	// KYC
	// ========================

	@Column(length = 50)
	private String drivingLicenseNumber;

	private LocalDate drivingLicenseExpiry;

	@Column(length = 50)
	private String drivingLicenseState;

	@Column(length = 20)
	private String aadharNumber;

	@Column(precision = 10, scale = 2)
	private BigDecimal fixedDepositAmount;

	// ========================
	// FILE PATHS
	// ========================

	@Column(length = 255)
	private String addressProofPath;

	@Column(length = 255)
	private String drivingLicenseFilePath;

	// ========================
	// AUDIT
	// ========================

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	protected void prePersist() {
		if (createdAt == null) {
			createdAt = LocalDateTime.now();
		}
	}

	// ========================
	// COMPATIBILITY METHODS ✅
	// ========================

	// For old service code
	public void setStartDate(LocalDateTime dt) {
		this.pickupDateTime = dt;
	}

	public void setEndDate(LocalDateTime dt) {
		this.dropDateTime = dt;
	}

	public LocalDateTime getStartDate() {
		return pickupDateTime;
	}

	public LocalDateTime getEndDate() {
		return dropDateTime;
	}

	// ========================
	// GETTERS & SETTERS
	// ========================

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
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

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}

	public boolean isPaymentConfirmed() {
		return paymentConfirmed;
	}

	public void setPaymentConfirmed(boolean paymentConfirmed) {
		this.paymentConfirmed = paymentConfirmed;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getPickupLocation() {
		return pickupLocation;
	}

	public void setPickupLocation(String pickupLocation) {
		this.pickupLocation = pickupLocation;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public LocalDateTime getPickupDateTime() {
		return pickupDateTime;
	}

	public void setPickupDateTime(LocalDateTime pickupDateTime) {
		this.pickupDateTime = pickupDateTime;
	}

	public LocalDateTime getDropDateTime() {
		return dropDateTime;
	}

	public void setDropDateTime(LocalDateTime dropDateTime) {
		this.dropDateTime = dropDateTime;
	}

	public String getDrivingLicenseNumber() {
		return drivingLicenseNumber;
	}

	public void setDrivingLicenseNumber(String drivingLicenseNumber) {
		this.drivingLicenseNumber = drivingLicenseNumber;
	}

	public LocalDate getDrivingLicenseExpiry() {
		return drivingLicenseExpiry;
	}

	public void setDrivingLicenseExpiry(LocalDate drivingLicenseExpiry) {
		this.drivingLicenseExpiry = drivingLicenseExpiry;
	}

	public String getDrivingLicenseState() {
		return drivingLicenseState;
	}

	public void setDrivingLicenseState(String drivingLicenseState) {
		this.drivingLicenseState = drivingLicenseState;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public BigDecimal getFixedDepositAmount() {
		return fixedDepositAmount;
	}

	public void setFixedDepositAmount(BigDecimal fixedDepositAmount) {
		this.fixedDepositAmount = fixedDepositAmount;
	}

	public String getAddressProofPath() {
		return addressProofPath;
	}

	public void setAddressProofPath(String addressProofPath) {
		this.addressProofPath = addressProofPath;
	}

	public String getDrivingLicenseFilePath() {
		return drivingLicenseFilePath;
	}

	public void setDrivingLicenseFilePath(String drivingLicenseFilePath) {
		this.drivingLicenseFilePath = drivingLicenseFilePath;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
