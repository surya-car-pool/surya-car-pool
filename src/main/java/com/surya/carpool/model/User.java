package com.surya.carpool.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// BASIC LOGIN / OWNER
	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, unique = true)
	private String phone;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private boolean enabled = true;

	// OWNER DETAILS
	private String ownerAddress;
	private String ownerCity;
	private String ownerState;
	private String ownerPincode;
	private String ownerAadharNo;
	private String ownerPanNo;

	// ====== ONE-TO-MANY: USER (OWNER) -> CARS ======
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<Car> cars = new ArrayList<>();

	public User() {
	}

	// helper to keep both sides in sync
	public void addCar(Car car) {
		cars.add(car);
		car.setOwner(this);
	}

	public void removeCar(Car car) {
		cars.remove(car);
		car.setOwner(null);
	}

	// ====== GETTERS & SETTERS ======

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getOwnerAddress() {
		return ownerAddress;
	}

	public void setOwnerAddress(String ownerAddress) {
		this.ownerAddress = ownerAddress;
	}

	public String getOwnerCity() {
		return ownerCity;
	}

	public void setOwnerCity(String ownerCity) {
		this.ownerCity = ownerCity;
	}

	public String getOwnerState() {
		return ownerState;
	}

	public void setOwnerState(String ownerState) {
		this.ownerState = ownerState;
	}

	public String getOwnerPincode() {
		return ownerPincode;
	}

	public void setOwnerPincode(String ownerPincode) {
		this.ownerPincode = ownerPincode;
	}

	public String getOwnerAadharNo() {
		return ownerAadharNo;
	}

	public void setOwnerAadharNo(String ownerAadharNo) {
		this.ownerAadharNo = ownerAadharNo;
	}

	public String getOwnerPanNo() {
		return ownerPanNo;
	}

	public void setOwnerPanNo(String ownerPanNo) {
		this.ownerPanNo = ownerPanNo;
	}

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars.clear();
		if (cars != null) {
			for (Car c : cars) {
				addCar(c); // uses helper to set owner
			}
		}
	}
}
