package com.surya.carpool.car.service;

import java.util.List;

import com.surya.carpool.car.model.Car;

public interface CarService {

	Car addCar(Car car);

	Car getCarById(Long id);

	List<Car> getAllCars();

	void deleteCar(Long id);

	// ✅ ADD THIS
	void markCarAsBooked(Long carId);
}
