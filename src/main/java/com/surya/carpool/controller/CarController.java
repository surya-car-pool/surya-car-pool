package com.surya.carpool.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.surya.carpool.model.Car;
import com.surya.carpool.model.CarStatusUpdateRequest;
import com.surya.carpool.service.CarService;

@RestController
@RequestMapping("/api/cars")
public class CarController {

	private final CarService carService;

	public CarController(CarService carService) {
		this.carService = carService;
	}

	@PutMapping("/{carId}/status")
	public ResponseEntity<Car> updateCarStatus(@PathVariable Long carId, @RequestBody CarStatusUpdateRequest request) {

		Car updatedCar = carService.updateStatus(carId, request.getStatus());
		return ResponseEntity.ok(updatedCar);
	}
}
