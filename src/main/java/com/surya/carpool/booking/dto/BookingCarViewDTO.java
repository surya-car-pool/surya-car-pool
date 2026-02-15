package com.surya.carpool.booking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.surya.carpool.car.model.CarStatus;

public class BookingCarViewDTO {

	private Long carId;
	private String carMake;
	private String registrationNo;
	private BigDecimal perDayRent;
	private CarStatus status;
	private String customerName;
	private String pickupLocation;
	private LocalDateTime pickupDateTime;
	private LocalDateTime dropDateTime;

	public BookingCarViewDTO(
	        Long carId,
	        String carMake,
	        String registrationNo,
	        BigDecimal perDayRent,
	        CarStatus status,
	        String customerName,
	        String pickupLocation,
	        LocalDateTime pickupDateTime,
	        LocalDateTime dropDateTime) {

		this.carId = carId;
		this.carMake = carMake;
		this.registrationNo = registrationNo;
		this.perDayRent = perDayRent;
		this.status = status;
		this.customerName = customerName;
		this.pickupLocation = pickupLocation;
		this.pickupDateTime = pickupDateTime;
		this.dropDateTime = dropDateTime;
	}
	public Long getCarId() {
		return carId;
	}

	public String getCarMake() { return carMake; }
	public String getRegistrationNo() { return registrationNo; }


	public BigDecimal getPerDayRent() {
		return perDayRent;
	}

	public CarStatus getStatus() {
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
