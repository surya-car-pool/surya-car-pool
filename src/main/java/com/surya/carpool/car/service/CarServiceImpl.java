package com.surya.carpool.car.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surya.carpool.car.model.Car;
import com.surya.carpool.car.model.CarStatus;
import com.surya.carpool.car.repository.CarRepository;

@Service
public class CarServiceImpl implements CarService {

	@Autowired
	private CarRepository carRepository;

	@Override
	public Car addCar(Car car) {

		if (car.getCarNumber() == null || car.getCarNumber().isEmpty()) {
			throw new RuntimeException("Car number required");
		}

		car.setStatus(CarStatus.AVAILABLE);

		return carRepository.save(car);
	}

	@Override
	public Car getCarById(Long id) {
		return carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found"));
	}

	@Override
	public List<Car> getAllCars() {
		return carRepository.findAll();
	}

	@Override
	public void deleteCar(Long id) {
		carRepository.deleteById(id);
	}

	// ✅ IMPLEMENTED METHOD
	@Override
	public void markCarAsBooked(Long carId) {

		Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));

		car.setStatus(CarStatus.BOOKED);

		carRepository.save(car);
	}
}
