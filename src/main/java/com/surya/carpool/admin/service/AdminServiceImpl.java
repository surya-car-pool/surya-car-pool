package com.surya.carpool.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.surya.carpool.booking.model.Booking;
import com.surya.carpool.booking.repository.BookingRepository;
import com.surya.carpool.car.model.Car;
import com.surya.carpool.car.service.CarService;
import com.surya.carpool.user.model.User;
import com.surya.carpool.user.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private CarService carService;

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public List<Booking> getAllBookings() {
		return bookingRepository.findAll();
	}

	public Car addCar(Car car) {
		return carService.addCar(car);
	}

}
