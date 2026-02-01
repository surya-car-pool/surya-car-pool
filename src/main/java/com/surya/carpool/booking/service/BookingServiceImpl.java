package com.surya.carpool.booking.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.surya.carpool.booking.dto.BookingCarViewDTO;
import com.surya.carpool.booking.model.Booking;
import com.surya.carpool.booking.model.BookingForm;
import com.surya.carpool.booking.model.BookingStatus;
import com.surya.carpool.booking.repository.BookingRepository;
import com.surya.carpool.car.model.Car;
import com.surya.carpool.car.repository.CarRepository;
import com.surya.carpool.user.model.ApprovalStatus;
import com.surya.carpool.user.model.Role;
import com.surya.carpool.user.model.User;
import com.surya.carpool.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

	private final BookingRepository bookingRepository;
	private final CarRepository carRepository;
	private final UserRepository userRepository;

	public BookingServiceImpl(BookingRepository bookingRepository, CarRepository carRepository,
			UserRepository userRepository) {
		this.bookingRepository = bookingRepository;
		this.carRepository = carRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Booking createBooking(BookingForm form) throws IOException {

		User currentUser = getCurrentUser();

		if (currentUser.getRole() == Role.OWNER) {
			throw new IllegalStateException("Car owners cannot book cars");
		}

		if (currentUser.getApprovalStatus() != ApprovalStatus.APPROVED) {
			throw new IllegalStateException("User account not approved for booking");
		}

		Car car = carRepository.findById(form.getCarId())
				.orElseThrow(() -> new IllegalArgumentException("Car not found"));

		if (existsActiveBookingForCar(car.getId())) {
			throw new IllegalStateException("Car already booked");
		}

		Booking booking = new Booking();
		booking.setCar(car);
		booking.setStartDate(form.getStartDate());
		booking.setEndDate(form.getEndDate());
		booking.setStatus(BookingStatus.ACTIVE);

		return bookingRepository.save(booking);
	}

	@Override
	public Booking createBookingFromEntity(Booking booking) {
		return bookingRepository.save(booking);
	}

	@Override
	public List<Booking> getAll() {
		return bookingRepository.findAll();
	}

	@Override
	public boolean existsActiveBookingForCar(Long carId) {
		return bookingRepository.existsByCar_IdAndStatus(carId, BookingStatus.ACTIVE);
	}

	@Override
	public List<BookingCarViewDTO> getAllBookingCarDetails() {
		return bookingRepository.fetchBookingCarDetails();
	}

	// =========================
	// ✅ FIXED Helper
	// =========================
	private User getCurrentUser() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null
				|| "anonymousUser".equals(auth.getPrincipal())) {
			throw new RuntimeException("User not logged in");
		}

		Object principal = auth.getPrincipal();

		// Your project stores User directly in SecurityContext
		if (principal instanceof User) {
			return (User) principal;
		}

		// Fallback (if only email is stored)
		String email = auth.getName();
		return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Logged in user not found"));
	}

	@Override
	@Transactional
	public Booking createBookingFromForm(BookingForm form) {

		Booking booking = new Booking();

		Car car = carRepository.findById(form.getCarId()).orElseThrow(() -> new RuntimeException("Car not found"));
		booking.setCar(car);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
			throw new RuntimeException("User must be logged in to book a car");
		}

		User user = (User) auth.getPrincipal();
		booking.setCustomer(user);

		booking.setCustomerName(form.getCustomerName());
		booking.setEmail(form.getEmail());
		booking.setPhone(form.getPhone());
		booking.setPickupLocation(form.getPickupLocation());
		booking.setPickupDateTime(form.getPickupDateTime());
		booking.setDropDateTime(form.getDropDateTime());
		booking.setAmount(form.getAmount());
		booking.setNotes(form.getNotes());
		booking.setStatus(BookingStatus.ACTIVE);

		return bookingRepository.save(booking);
	}

	@Override
	public List<Booking> getBookingsForCurrentUser() {
		User currentUser = getCurrentUser();
		return bookingRepository.findByCustomer_Id(currentUser.getId());
	}

	@Override
	@Transactional
	public void cancelBooking(Long bookingId) {

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		// Rule 1: Only ACTIVE
		if (!booking.getStatus().name().equals("ACTIVE")) {
			throw new RuntimeException("This booking cannot be cancelled.");
		}

		// Rule 2: Pickup time must be in future
		// ✅ Allow cancel only BEFORE pickup time
		if (LocalDateTime.now().isAfter(booking.getPickupDateTime())) {
		    throw new RuntimeException("Booking cannot be cancelled after pickup time.");
		}


		booking.setStatus(BookingStatus.CANCELLED);
		bookingRepository.save(booking);
	}

	@Override
	@Transactional
	public void extendBooking(Long bookingId, LocalDateTime newDropDate) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found"));

		booking.setDropDateTime(newDropDate);
		bookingRepository.save(booking);
	}

	@Override
	@Transactional
	public void rescheduleBooking(Long bookingId, LocalDateTime newPickup, LocalDateTime newDrop) {

		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Booking not found with id " + bookingId));

		if (!booking.getStatus().name().equals("ACTIVE")) {
			throw new RuntimeException("Only ACTIVE bookings can be rescheduled");
		}

		if (newPickup.isBefore(LocalDateTime.now())) {
			throw new RuntimeException("Pickup date cannot be in the past");
		}

		if (!newDrop.isAfter(newPickup)) {
			throw new RuntimeException("Drop date must be after pickup date");
		}

		booking.setPickupDateTime(newPickup);
		booking.setDropDateTime(newDrop);

		bookingRepository.save(booking);
	}

	@Override
	public Booking getBookingById(Long id) {
		return bookingRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
	}

}
