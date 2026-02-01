package com.surya.carpool.owner.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.surya.carpool.car.model.Car;
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

	public OwnerService(UserRepository userRepository, FileStorageService fileStorageService) {
		this.userRepository = userRepository;
		this.fileStorageService = fileStorageService;
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
}
