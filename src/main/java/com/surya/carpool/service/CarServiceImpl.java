package com.surya.carpool.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surya.carpool.model.Car;
import com.surya.carpool.model.CarStatus;
import com.surya.carpool.repository.CarRepository;

@Service
@Transactional // ✅ ADD THIS
public class CarServiceImpl implements CarService {

	private final CarRepository carRepository;

	public CarServiceImpl(CarRepository carRepository) {
		this.carRepository = carRepository;
	}

	@Override
	public List<Car> findActiveCars() {
		return carRepository.findByActiveTrue();
	}

	@Override
	public Car findById(Long id) {
		return carRepository.findById(id).orElse(null);
	}

	@Override
	public Car save(Car car) {
		return carRepository.save(car);
	}

	@Override
	public Car getCarById(Long id) {
		return carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found"));
	}

	@Override
	public void markCarAsBooked(Long carId) {
		Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));

		car.setStatus(CarStatus.BOOKED);

		carRepository.save(car);
	}

}
