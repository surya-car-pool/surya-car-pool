package com.surya.carpool.booking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.surya.carpool.booking.dto.BookingCarViewDTO;
import com.surya.carpool.booking.model.Booking;
import com.surya.carpool.booking.model.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	// ==========================
	// Check if car already booked (ACTIVE)
	// ==========================
	boolean existsByCar_IdAndStatus(Long carId, BookingStatus status);

	// ==========================
	// DTO Projection Query
	// ==========================
	@Query("""
		    SELECT new com.surya.carpool.booking.dto.BookingCarViewDTO(
		        c.id,
		        c.make,
		        c.registrationNo,
		        c.perDayRent,
		        c.status,
		        b.customerName,
		        b.pickupLocation,
		        b.pickupDateTime,
		        b.dropDateTime
		    )
		    FROM Booking b
		    JOIN b.car c
		""")
		List<BookingCarViewDTO> fetchBookingCarDetails();


	// ==========================
	// Optional raw query
	// ==========================
	@Query("""
			    SELECT
			        c.id,
			        c.carName,
			        c.carNumber,
			        c.perDayRent,
			        c.status,
			        b.customerName,
			        b.pickupLocation,
			        b.pickupDateTime,
			        b.dropDateTime
			    FROM Booking b
			    JOIN b.car c
			""")
	List<Object[]> findAllBookingCarDetailsRaw();
	
	List<Booking> findByCustomer_Id(Long customerId);


}
