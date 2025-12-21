package com.surya.carpool.service;

import java.util.List;

import com.surya.carpool.model.Car;
import com.surya.carpool.model.CarStatus;

public interface CarService {

    List<Car> findActiveCars();

    Car findById(Long id);

    Car save(Car car);
    
    Car getCarById(Long id);

    void markCarAsBooked(Long carId);
    Car updateStatus(Long carId, CarStatus status);
}
