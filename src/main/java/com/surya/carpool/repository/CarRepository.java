package com.surya.carpool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

	List<Car> findByActiveTrue();

	List<Car> findByStatus(CarStatus status);

	@Modifying
	@Query("""
			    UPDATE Car c
			    SET c.status = com.surya.carpool.model.CarStatus.BOOKED
			    WHERE c.id = :carId
			      AND c.status = com.surya.carpool.model.CarStatus.AVAILABLE
			""")
	int markCarAsBooked(@Param("carId") Long carId);

}
