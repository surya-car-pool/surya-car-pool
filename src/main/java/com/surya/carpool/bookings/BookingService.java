package com.surya.carpool.bookings;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.surya.carpool.dto.BookingCarViewDTO;
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

	public boolean existsActiveBookingForCar(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<BookingCarViewDTO> getAllBookingCarDetails() {

		List<Object[]> rows = bookingRepository.findAllBookingCarDetailsRaw();

		return rows.stream().map(r -> {
			Long carId = ((Number) r[0]).longValue();
			String carName = (String) r[1];
			String carNumber = (String) r[2];
			Double perDayRent = ((Number) r[3]).doubleValue();
			String status = (String) r[4];
			String customerName = (String) r[5];
			String pickupLocation = (String) r[6];
			Timestamp pickupTs = (Timestamp) r[7];
			Timestamp dropTs = (Timestamp) r[8];
			System.out.println("CAR NAME FROM DB = " + r[1]);

			return new BookingCarViewDTO(carId, carName, carNumber, perDayRent, status, customerName, pickupLocation,
					pickupTs.toLocalDateTime(), dropTs.toLocalDateTime());
		}).toList();
	}

}
