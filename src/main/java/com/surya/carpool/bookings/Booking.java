package com.surya.carpool.bookings;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long carId;

	private String customerName;
	private String email;
	private String phone;
	private String status;

	private String carModel;

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

	private boolean paymentConfirmed;
	@Column(length = 500)
	private String customerAddress;

	@Column(length = 500)
	private String pickupLocation;

	@Column(length = 1000)
	private String notes;

	private LocalDateTime pickupDateTime;
	private LocalDateTime dropDateTime;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String paymentMethod; // UPI / CARD / CASH
	private BigDecimal amount;

	// KYC fields
	private String drivingLicenseNumber;
	private LocalDate drivingLicenseExpiry;
	private String drivingLicenseState;
	private String aadharNumber;
	private BigDecimal fixedDepositAmount;

	// Stored file paths (relative)
	private String addressProofPath;
	private String drivingLicenseFilePath;

	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {
		if (createdAt == null) {
			createdAt = LocalDateTime.now();
		}
	}

	// ---------- getters & setters ----------

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
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

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
