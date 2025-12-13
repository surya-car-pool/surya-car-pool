package com.surya.carpool.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * PaymentReceipt model used by Thymeleaf templates. Includes pickupDateTime
 * field and a formatted convenience getter.
 */
public class PaymentReceipt {

	private String receiptNumber;
	private LocalDateTime paymentDateTime;
	private String status;
	private String paymentMethod;
	private BigDecimal amountPaid;

	private String customerName;
	private String customerEmail;
	private String customerPhone;

	private Long carId;
	private String pickupLocation;
	// <-- NEW field required by template
	private LocalDateTime pickupDateTime;

	private String transactionId;
	private String gateway;

	private Double baseFare;
	private Double taxes;
	private Double discount;

	// --- getters / setters ---

	public String getReceiptNumber() {
		return receiptNumber;
	}

	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}

	public LocalDateTime getPaymentDateTime() {
		return paymentDateTime;
	}

	public void setPaymentDateTime(LocalDateTime paymentDateTime) {
		this.paymentDateTime = paymentDateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	
	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
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

	// NEW: pickupDateTime getter/setter
	public LocalDateTime getPickupDateTime() {
		return pickupDateTime;
	}

	public void setPickupDateTime(LocalDateTime pickupDateTime) {
		this.pickupDateTime = pickupDateTime;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public Double getBaseFare() {
		return baseFare;
	}

	public void setBaseFare(Double baseFare) {
		this.baseFare = baseFare;
	}

	public Double getTaxes() {
		return taxes;
	}

	public void setTaxes(Double taxes) {
		this.taxes = taxes;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	// Convenience formatted getter (optional for templates)
	public String getPickupDateTimeFormatted() {
		if (this.pickupDateTime == null)
			return "";
		return this.pickupDateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
	}

	@Override
	public int hashCode() {
		return Objects.hash(receiptNumber, transactionId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		PaymentReceipt other = (PaymentReceipt) obj;
		return Objects.equals(receiptNumber, other.receiptNumber) && Objects.equals(transactionId, other.transactionId);
	}
}
