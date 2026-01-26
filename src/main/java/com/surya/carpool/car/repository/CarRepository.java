package com.surya.carpool.car.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.surya.carpool.car.model.Car;
import com.surya.carpool.car.model.CarStatus;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

	/**
	 * Returns cars whose owner is enabled (active).
	 */
	List<Car> findByOwnerEnabledTrue();

	/**
	 * Returns only cars marked as active.
	 */
	List<Car> findByActiveTrue();

	/**
	 * Returns cars by exact status.
	 */
	List<Car> findByStatus(CarStatus status);

	/**
	 * Returns cars that are: - Active - Owner enabled - Status = AVAILABLE
	 *
	 * Best for booking UI.
	 */
	List<Car> findByActiveTrueAndOwnerEnabledTrueAndStatus(CarStatus status);

}
