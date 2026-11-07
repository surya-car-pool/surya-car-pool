package com.surya.carpool.owner.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.surya.carpool.car.model.Car;
import com.surya.carpool.owner.service.OwnerService;
import com.surya.carpool.owner.service.OwnerTermsService;
import com.surya.carpool.user.model.Role;
import com.surya.carpool.user.model.User;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

	private final OwnerService ownerService;
	private final OwnerTermsService ownerTermsService;

	public OwnerController(OwnerService ownerService, OwnerTermsService ownerTermsService) {
		this.ownerService = ownerService;
		this.ownerTermsService = ownerTermsService;
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createOwner(@RequestPart("user") User user,
			@RequestPart(value = "carImage", required = false) MultipartFile carImage,
			@RequestPart(value = "rcImage", required = false) MultipartFile rcImage) throws IOException {

		ownerService.createOwnerWithCar(user, carImage, rcImage);
		return ResponseEntity.ok().build();
	}

	@PostMapping(path = "/vehicles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> addVehicle(@RequestPart("car") Car car,
			@RequestPart("frontPhoto") MultipartFile frontPhoto,
			@RequestPart("rcImage") MultipartFile rcImage,
			@RequestPart("insuranceDocument") MultipartFile insuranceDocument,
			@RequestPart("pollutionCertificate") MultipartFile pollutionCertificate,
			@RequestPart("drivingLicense") MultipartFile drivingLicense,
			@RequestPart("rearPhoto") MultipartFile rearPhoto,
			@RequestPart("leftPhoto") MultipartFile leftPhoto,
			@RequestPart("rightPhoto") MultipartFile rightPhoto,
			@RequestPart("interiorPhoto") MultipartFile interiorPhoto,
			@AuthenticationPrincipal User authenticatedUser) throws IOException {
		if (authenticatedUser.getRole() != Role.USER && authenticatedUser.getRole() != Role.OWNER) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Owner account required.");
		}
		if (!ownerTermsService.hasAcceptedCurrentTerms(authenticatedUser)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
					.body("Accept the current vehicle owner agreement before submitting a vehicle.");
		}
		try {
			return ResponseEntity.ok(ownerService.addVehicle(authenticatedUser.getId(), car, frontPhoto, rcImage,
					insuranceDocument, pollutionCertificate, drivingLicense, rearPhoto, leftPhoto, rightPhoto,
					interiorPhoto));
		} catch (IllegalArgumentException exception) {
			return ResponseEntity.badRequest().body(exception.getMessage());
		}
	}
}
