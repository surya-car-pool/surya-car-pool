package com.surya.carpool.user.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.surya.carpool.car.model.Car;

import jakarta.persistence.*;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "email"),
		@UniqueConstraint(columnNames = "phone") })
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// ======================
	// BASIC LOGIN DETAILS
	// ======================

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, unique = true, length = 120)
	private String email;

	@Column(nullable = false, unique = true, length = 20)
	private String phone;

	@Column(nullable = false, length = 255)
	private String password;

	// ======================
	// SECURITY
	// ======================

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Role role = Role.USER; // USER / OWNER / ADMIN

	@Column(nullable = false)
	private boolean enabled = true;

	// ✅ NEW: approval status for car owners
	@Enumerated(EnumType.STRING)
	@Column(name = "approval_status", nullable = false, length = 20)
	private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

	// ======================
	// OWNER DETAILS (only if role == OWNER)
	// ======================

	@Column(length = 255)
	private String ownerAddress;

	@Column(length = 100)
	private String ownerCity;

	@Column(length = 100)
	private String ownerState;

	@Column(length = 10)
	private String ownerPincode;

	@Column(length = 20)
	private String ownerAadharNo;

	@Column(length = 20)
	private String ownerPanNo;

	// ======================
	// FILE PATHS (optional)
	// ======================

	@Column(name = "owner_car_image_path", length = 255)
	private String carImagePath;

	@Column(name = "owner_rc_image_path", length = 255)
	private String rcImagePath;

	// ======================
	// RELATIONSHIP
	// ======================

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<Car> cars = new ArrayList<>();

	// ======================
	// CONSTRUCTORS
	// ======================

	public User() {
	}

	// ======================
	// HELPER METHODS
	// ======================

	public void addCar(Car car) {
		if (car != null) {
			cars.add(car);
			car.setOwner(this);
		}
	}

	public void removeCar(Car car) {
		if (car != null) {
			cars.remove(car);
			car.setOwner(null);
		}
	}

	// ======================
	// GETTERS & SETTERS
	// ======================

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public ApprovalStatus getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(ApprovalStatus approvalStatus) {
		this.approvalStatus = approvalStatus;
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

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars.clear();
		if (cars != null) {
			for (Car c : cars) {
				addCar(c);
			}
		}
	}
}
