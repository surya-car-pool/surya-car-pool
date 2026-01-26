package com.surya.carpool.booking.service;

import java.io.IOException;
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

		// ❌ Block owners from booking
		if (currentUser.getRole() == Role.OWNER) {
			throw new IllegalStateException("Car owners cannot book cars");
		}

		// ❌ Block unapproved accounts
		if (currentUser.getApprovalStatus() != ApprovalStatus.APPROVED) {
			throw new IllegalStateException("User account not approved for booking");
		}

		Car car = carRepository.findById(form.getCarId())
				.orElseThrow(() -> new IllegalArgumentException("Car not found"));

		if (existsActiveBookingForCar(car.getId())) {
			throw new IllegalStateException("Car already booked");
		}

		Booking booking = new Booking();

		// ✅ IMPORTANT:
		// Your Booking entity does NOT have setUser(...)
		// So we only set car + dates + status
		// (booking user is handled elsewhere in your system)

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
	// Helper
	// =========================
	private User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();

		return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Logged in user not found"));
	}

	@Override
	@Transactional
	public Booking createBookingFromForm(BookingForm form) {

		Booking booking = new Booking();

		// Car
		Car car = carRepository.findById(form.getCarId()).orElseThrow(() -> new RuntimeException("Car not found"));
		booking.setCar(car);

		// ============================
		// 🔐 SET LOGGED IN USER
		// ============================
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
			throw new RuntimeException("User must be logged in to book a car");
		}

		User user = (User) auth.getPrincipal();
		booking.setCustomer(user); // ✅ THIS FIXES YOUR ERROR

		// ============================
		// Booking details
		// ============================
		booking.setCustomerName(form.getCustomerName());
		booking.setEmail(form.getEmail());
		booking.setPhone(form.getPhone());
		booking.setPickupLocation(form.getPickupLocation());
		booking.setPickupDateTime(form.getPickupDateTime());
		booking.setDropDateTime(form.getDropDateTime());
		booking.setAmount(form.getAmount());
		booking.setNotes(form.getNotes());
		booking.setStatus(BookingStatus.ACTIVE);

		Booking saved = bookingRepository.save(booking);

		return saved;
	}

}
