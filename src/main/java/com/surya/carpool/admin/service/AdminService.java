package com.surya.carpool.admin.service;

import java.util.List;

import com.surya.carpool.booking.model.Booking;
import com.surya.carpool.car.model.Car;
import com.surya.carpool.user.model.User;

public interface AdminService {

	Car addCar(Car car);

	List<User> getAllUsers();

	List<Booking> getAllBookings();
}
