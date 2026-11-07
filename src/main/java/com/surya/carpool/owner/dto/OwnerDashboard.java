package com.surya.carpool.owner.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OwnerDashboard {

	private final String name;
	private final long vehicleCount;
	private final long activeBookingCount;
	private final BigDecimal totalEarnings;
	private final LocalDateTime memberSince;
	private final String verificationStatus;
	private final LocalDateTime termsAcceptedOn;

	public OwnerDashboard(String name, long vehicleCount, long activeBookingCount, BigDecimal totalEarnings,
			LocalDateTime memberSince, String verificationStatus, LocalDateTime termsAcceptedOn) {
		this.name = name;
		this.vehicleCount = vehicleCount;
		this.activeBookingCount = activeBookingCount;
		this.totalEarnings = totalEarnings;
		this.memberSince = memberSince;
		this.verificationStatus = verificationStatus;
		this.termsAcceptedOn = termsAcceptedOn;
	}

	public String getName() {
		return name;
	}

	public long getVehicleCount() {
		return vehicleCount;
	}

	public long getActiveBookingCount() {
		return activeBookingCount;
	}

	public BigDecimal getTotalEarnings() {
		return totalEarnings;
	}

	public LocalDateTime getMemberSince() {
		return memberSince;
	}

	public String getVerificationStatus() {
		return verificationStatus;
	}

	public LocalDateTime getTermsAcceptedOn() {
		return termsAcceptedOn;
	}
}
