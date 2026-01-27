package com.surya.carpool.booking.service;

import java.io.IOException;
import java.util.List;

import com.surya.carpool.booking.dto.BookingCarViewDTO;
import com.surya.carpool.booking.model.Booking;
import com.surya.carpool.booking.model.BookingForm;

public interface BookingService {

	Booking createBooking(BookingForm form) throws IOException;

	Booking createBookingFromEntity(Booking booking);

	List<Booking> getAll();

	boolean existsActiveBookingForCar(Long carId);

	List<BookingCarViewDTO> getAllBookingCarDetails();

	Booking createBookingFromForm(BookingForm form);

	List<Booking> getBookingsForCurrentUser();


}
