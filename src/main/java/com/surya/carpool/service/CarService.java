package com.surya.carpool.service;

import com.surya.carpool.model.Car;
import java.util.List;

public interface CarService {

    List<Car> findActiveCars();

    Car findById(Long id);

    Car save(Car car);
}
