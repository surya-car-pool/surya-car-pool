package com.surya.carpool.admin.service;

import java.util.List;

import com.surya.carpool.car.model.Car;

public interface AdminCarService {
	List<Car> getAllCars();

	void approveCar(Long carId);

	void disableCar(Long carId);

	void deleteCar(Long carId);
}
