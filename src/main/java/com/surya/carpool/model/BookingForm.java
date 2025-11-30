package com.surya.carpool.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

public class BookingForm {

	private Long carId;

	private String customerName;
	private String email;
	private String phone;
	private String customerAddress;
	private String pickupLocation;
	private String notes;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime pickupDateTime;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dropDateTime;

	private String paymentMethod;
	private BigDecimal amount;

	private String drivingLicenseNumber;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate drivingLicenseExpiry;

	private String drivingLicenseState;
	private String aadharNumber;
	private BigDecimal fixedDepositAmount;

	private MultipartFile addressProofFile;
	private MultipartFile drivingLicenseFile;

	// ---------- getters & setters ----------

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

	public MultipartFile getAddressProofFile() {
		return addressProofFile;
	}

	public void setAddressProofFile(MultipartFile addressProofFile) {
		this.addressProofFile = addressProofFile;
	}

	public MultipartFile getDrivingLicenseFile() {
		return drivingLicenseFile;
	}

	public void setDrivingLicenseFile(MultipartFile drivingLicenseFile) {
		this.drivingLicenseFile = drivingLicenseFile;
	}
}
