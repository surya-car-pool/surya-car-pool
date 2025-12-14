package com.surya.carpool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.surya.carpool.model.Car;
import com.surya.carpool.model.CarStatus;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

	/**
	 * Returns cars whose owner is enabled (active). Spring Data will translate this
	 * to a join on owner.enabled = true.
	 */
	List<Car> findByOwnerEnabledTrue();

	// Optional: existing / future queries
	// List<Car> findByOwnerId(Long ownerId);
	List<Car> findByActiveTrue(); 
	List<Car> findByStatus(CarStatus status);
}
