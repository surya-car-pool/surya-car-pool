package com.surya.carpool.dto;

import java.time.LocalDateTime;

public class BookingCarViewDTO {

	private long carId;
	private String carName;
	private String carNumber;
	private double perDayRent;
	private String status;
	private String customerName;
	private String pickupLocation;
	private LocalDateTime pickupDateTime;
	private LocalDateTime dropDateTime;

	// ✅ REQUIRED constructor (matches service call exactly)
	public BookingCarViewDTO(long carId, String carName, String carNumber, double perDayRent, String status,
			String customerName, String pickupLocation, LocalDateTime pickupDateTime, LocalDateTime dropDateTime) {

		this.carId = carId;
		this.carName = carName;
		this.carNumber = carNumber;
		this.perDayRent = perDayRent;
		this.status = status;
		this.customerName = customerName;
		this.pickupLocation = pickupLocation;
		this.pickupDateTime = pickupDateTime;
		this.dropDateTime = dropDateTime;
	}

	// getters only
	public long getCarId() {
		return carId;
	}

	public String getCarName() {
		return carName;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public double getPerDayRent() {
		return perDayRent;
	}

	public String getStatus() {
		return status;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getPickupLocation() {
		return pickupLocation;
	}

	public LocalDateTime getPickupDateTime() {
		return pickupDateTime;
	}

	public LocalDateTime getDropDateTime() {
		return dropDateTime;
	}
}
