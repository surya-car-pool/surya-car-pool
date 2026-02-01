package com.surya.carpool.car.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.surya.carpool.car.model.Car;
import com.surya.carpool.car.service.CarService;

@RestController
@RequestMapping("/api/cars")
public class CarController {

	@Autowired
	private CarService carService;

	@PostMapping
	public Car addCar(@RequestBody Car car) {
		return carService.addCar(car);
	}

	@GetMapping("/{id}")
	public Car getCar(@PathVariable Long id) {
		return carService.getCarById(id);
	}

}
