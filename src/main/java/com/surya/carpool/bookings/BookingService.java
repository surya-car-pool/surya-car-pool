package com.surya.carpool.bookings;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.surya.carpool.model.BookingForm;
import com.surya.carpool.service.FileStorageService;

@Service
public class BookingService {

	private final BookingRepository bookingRepository;
	private final FileStorageService fileStorageService;

	public BookingService(BookingRepository bookingRepository, FileStorageService fileStorageService) {
		this.bookingRepository = bookingRepository;
		this.fileStorageService = fileStorageService;
	}

	@Transactional
	public Booking createBooking(BookingForm form) throws IOException {
		Booking booking = new Booking();

		booking.setCarId(form.getCarId());
		booking.setCustomerName(form.getCustomerName());
		booking.setEmail(form.getEmail());
		booking.setPhone(form.getPhone());
		booking.setCustomerAddress(form.getCustomerAddress());
		booking.setPickupLocation(form.getPickupLocation());
		booking.setNotes(form.getNotes());

		booking.setPickupDateTime(form.getPickupDateTime());
		booking.setDropDateTime(form.getDropDateTime());

		booking.setPaymentMethod(form.getPaymentMethod());
		booking.setAmount(form.getAmount());

		booking.setDrivingLicenseNumber(form.getDrivingLicenseNumber());
		booking.setDrivingLicenseExpiry(form.getDrivingLicenseExpiry());
		booking.setDrivingLicenseState(form.getDrivingLicenseState());
		booking.setAadharNumber(form.getAadharNumber());
		booking.setFixedDepositAmount(form.getFixedDepositAmount());

		// Store files
		MultipartFile addressProofFile = form.getAddressProofFile();
		MultipartFile drivingLicenseFile = form.getDrivingLicenseFile();

		String addressProofPath = fileStorageService.store(addressProofFile, "address_proof");
		String dlPath = fileStorageService.store(drivingLicenseFile, "driving_license");

		booking.setAddressProofPath(addressProofPath);
		booking.setDrivingLicenseFilePath(dlPath);

		return bookingRepository.save(booking);
	}

	@Transactional(readOnly = true)
	public List<Booking> getAll() {
		return bookingRepository.findAll();
	}

	/**
	 * Used by the JSON REST API /api/bookings when client sends a Booking entity
	 * without file uploads.
	 */
	@Transactional
	public Booking createBookingFromEntity(Booking booking) {
		return bookingRepository.save(booking);
	}
}
