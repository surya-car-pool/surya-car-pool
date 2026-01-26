package com.surya.carpool.car.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.surya.carpool.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cars")
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Basic info
	private String carName;
	private String carNumber;
	private String driverName;

	// Availability status
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CarStatus status = CarStatus.AVAILABLE;

	// Owner (User with role = OWNER)
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "owner_id", nullable = false)
	@JsonBackReference
	private User owner;

	// Car specifications
	private String make;
	private String model;
	private String variant;

	private Integer seats;

	@Column(nullable = false, unique = true)
	private String registrationNo;

	// Pricing
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal perDayRent;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal perKmRate;

	// Fuel & transmission
	private String fuelType; // PETROL / DIESEL / CNG / EV
	private String transmissionType; // MANUAL / AUTOMATIC

	// Images
	@Column(name = "car_image_path")
	private String carImagePath;

	@Column(name = "rc_image_path")
	private String rcImagePath;

	// Active flag
	@Column(nullable = false)
	private boolean active = true;

	// Constructors
	public Car() {
	}

	public Car(String make, String model, String variant, Integer seats) {
		this.make = make;
		this.model = model;
		this.variant = variant;
		this.seats = seats;
	}

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public CarStatus getStatus() {
		return status;
	}

	public void setStatus(CarStatus status) {
		this.status = status;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public BigDecimal getPerDayRent() {
		return perDayRent;
	}

	public void setPerDayRent(BigDecimal perDayRent) {
		this.perDayRent = perDayRent;
	}

	public BigDecimal getPerKmRate() {
		return perKmRate;
	}

	public void setPerKmRate(BigDecimal perKmRate) {
		this.perKmRate = perKmRate;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public String getTransmissionType() {
		return transmissionType;
	}

	public void setTransmissionType(String transmissionType) {
		this.transmissionType = transmissionType;
	}

	public String getCarImagePath() {
		return carImagePath;
	}

	public void setCarImagePath(String carImagePath) {
		this.carImagePath = carImagePath;
	}

	public String getRcImagePath() {
		return rcImagePath;
	}

	public void setRcImagePath(String rcImagePath) {
		this.rcImagePath = rcImagePath;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
