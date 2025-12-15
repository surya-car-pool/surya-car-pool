package com.surya.carpool.bookings;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	@Query(value = """
			    SELECT
			        c.id,
			        c.car_name,
			        c.car_number,
			        c.per_day_rent,
			        c.status,
			        b.customer_name,
			        b.pickup_location,
			        b.pickup_date_time,
			        b.drop_date_time
			    FROM cars c
			    JOIN bookings b ON b.car_id = c.id
			    WHERE c.status = 'BOOKED'
			      AND b.pickup_date_time = (
			          SELECT MAX(b2.pickup_date_time)
			          FROM bookings b2
			          WHERE b2.car_id = c.id
			      )
			""", nativeQuery = true)
	List<Object[]> findAllBookingCarDetailsRaw();

}
