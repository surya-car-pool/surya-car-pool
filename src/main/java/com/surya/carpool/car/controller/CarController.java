package com.surya.carpool.car.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.surya.carpool.car.model.Car;
import com.surya.carpool.car.service.CarService;
import com.surya.carpool.owner.service.OwnerTermsService;
import com.surya.carpool.user.model.Role;
import com.surya.carpool.user.model.User;

@RestController
@RequestMapping("/api/cars")
public class CarController {

	private final CarService carService;
	private final OwnerTermsService ownerTermsService;

	public CarController(CarService carService, OwnerTermsService ownerTermsService) {
		this.carService = carService;
		this.ownerTermsService = ownerTermsService;
	}

	@PostMapping
	public ResponseEntity<?> addCar(@RequestBody Car car, @AuthenticationPrincipal User user) {
		if (user.getRole() == Role.OWNER && !ownerTermsService.hasAcceptedCurrentTerms(user)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Accept the current vehicle owner agreement before submitting a vehicle.");
		}
		return ResponseEntity.ok(carService.addCar(car));
	}

	@GetMapping("/{id}")
	public Car getCar(@PathVariable Long id) {
		return carService.getCarById(id);
	}

}
