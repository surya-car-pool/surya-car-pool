package com.surya.carpool.owner.service;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.surya.carpool.car.model.Car;
import com.surya.carpool.car.model.CarStatus;
import com.surya.carpool.car.repository.CarRepository;
import com.surya.carpool.common.service.FileStorageService;
import com.surya.carpool.user.model.ApprovalStatus;
import com.surya.carpool.user.model.Role;
import com.surya.carpool.user.model.User;
import com.surya.carpool.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OwnerService {

	private final UserRepository userRepository;
	private final FileStorageService fileStorageService;
	private final CarRepository carRepository;

	public OwnerService(UserRepository userRepository, FileStorageService fileStorageService,
			CarRepository carRepository) {
		this.userRepository = userRepository;
		this.fileStorageService = fileStorageService;
		this.carRepository = carRepository;
	}

	@Transactional
	public void createOwnerWithCar(User user, MultipartFile carImage, MultipartFile rcImage) throws IOException {

		// 1. Mark as OWNER and pending approval
		user.setRole(Role.OWNER);
		user.setApprovalStatus(ApprovalStatus.PENDING);

		// 2. Attach cars + store files
		for (Car car : user.getCars()) {
			car.setOwner(user);

			if (carImage != null && !carImage.isEmpty()) {
				String carImagePath = fileStorageService.store(carImage, "car");
				car.setCarImagePath(carImagePath);
			}

			if (rcImage != null && !rcImage.isEmpty()) {
				String rcImagePath = fileStorageService.store(rcImage, "rc");
				car.setRcImagePath(rcImagePath);
			}
		}

		// 3. Save owner + cars (cascade)
		userRepository.save(user);
	}

	@Transactional
	public Car addVehicle(Long ownerId, Car car, MultipartFile carImage, MultipartFile rcImage,
			MultipartFile insuranceDocument, MultipartFile pollutionCertificate, MultipartFile drivingLicense,
			MultipartFile rearPhoto, MultipartFile leftPhoto, MultipartFile rightPhoto, MultipartFile interiorPhoto)
			throws IOException {
		if (car.getMake() == null || car.getMake().isBlank() || car.getModel() == null
				|| car.getModel().isBlank() || car.getYear() == null || car.getFuelType() == null
				|| car.getFuelType().isBlank() || car.getRegistrationNo() == null
				|| car.getRegistrationNo().isBlank() || car.getPerDayRent() == null || car.getPerKmRate() == null) {
			throw new IllegalArgumentException("Vehicle number, brand, model, year, fuel type, and rates are required");
		}
		var uploads = Stream.of(carImage, rcImage, insuranceDocument, pollutionCertificate, drivingLicense,
				rearPhoto, leftPhoto, rightPhoto, interiorPhoto).toList();
		if (uploads.stream().anyMatch(file -> file == null || file.isEmpty())) {
			throw new IllegalArgumentException(
					"Please upload the RC, insurance, pollution certificate, driving license, and all five vehicle photos");
		}
		if (uploads.stream().anyMatch(file -> file.getSize() > 10L * 1024 * 1024)) {
			throw new IllegalArgumentException("Each uploaded file must be 10 MB or smaller");
		}
		User owner = userRepository.findById(ownerId)
				.orElseThrow(() -> new IllegalArgumentException("Owner account not found"));
		if (owner.getRole() != Role.OWNER) {
			throw new IllegalArgumentException("Only owner accounts can add vehicles");
		}

		car.setOwner(owner);
		car.setStatus(CarStatus.AVAILABLE);
		car.setCarName(car.getMake() + " " + car.getModel());
		car.setCarNumber(car.getRegistrationNo());
		car.setCarImagePath(fileStorageService.store(carImage, "car"));
		car.setRcImagePath(fileStorageService.store(rcImage, "rc"));
		car.setInsuranceDocumentPath(fileStorageService.store(insuranceDocument, "insurance"));
		car.setPollutionCertificatePath(fileStorageService.store(pollutionCertificate, "pollution"));
		car.setDrivingLicensePath(fileStorageService.store(drivingLicense, "license"));
		car.setRearPhotoPath(fileStorageService.store(rearPhoto, "car-rear"));
		car.setLeftSidePhotoPath(fileStorageService.store(leftPhoto, "car-left"));
		car.setRightSidePhotoPath(fileStorageService.store(rightPhoto, "car-right"));
		car.setInteriorPhotoPath(fileStorageService.store(interiorPhoto, "car-interior"));
		owner.addCar(car);
		return carRepository.save(car);
	}
}
