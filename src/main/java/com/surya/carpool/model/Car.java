package com.surya.carpool.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	// BASIC CAR INFO
	@Column(nullable = false)
	private String make; // Maruti, Hyundai...

	@Column(nullable = false)
	private String model; // Swift, i20...

	private String variant; // VXI, Sportz...

	@Column(nullable = false, unique = true)
	private String registrationNo; // KA-01-AB-1234

	private Integer year; // 2020
	private String color;
	private Integer seats; // 4,5,7

	private String fuelType; 
	private String transmissionType;

	private Double perDayRent;
	private Double perKmRate;

	// OWNER (USER)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	 @JsonBackReference
	private User owner;

	public Car() {
	}

	// ====== GETTERS & SETTERS ======

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
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

	public Double getPerDayRent() {
		return perDayRent;
	}

	public void setPerDayRent(Double perDayRent) {
		this.perDayRent = perDayRent;
	}

	public Double getPerKmRate() {
		return perKmRate;
	}

	public void setPerKmRate(Double perKmRate) {
		this.perKmRate = perKmRate;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
}
