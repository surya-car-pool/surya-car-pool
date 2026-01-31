package com.surya.carpool.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.surya.carpool.car.model.Car;
import com.surya.carpool.car.repository.CarRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdminCarServiceImpl implements AdminCarService {

	private final CarRepository carRepository;

	public AdminCarServiceImpl(CarRepository carRepository) {
		this.carRepository = carRepository;
	}

	@Override
	public List<Car> getAllCars() {
		return carRepository.findAll();
	}

	@Override
	public void approveCar(Long carId) {
		Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
		car.setActive(true);
	}

	@Override
	public void disableCar(Long carId) {
		Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));
		car.setActive(false);
	}

	@Override
	public void deleteCar(Long carId) {
		carRepository.deleteById(carId);
	}
}
