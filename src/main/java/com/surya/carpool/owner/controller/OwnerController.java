package com.surya.carpool.owner.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.surya.carpool.owner.service.OwnerService;
import com.surya.carpool.user.model.User;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

	private final OwnerService ownerService;

	public OwnerController(OwnerService ownerService) {
		this.ownerService = ownerService;
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> createOwner(@RequestPart("user") User user,
			@RequestPart(value = "carImage", required = false) MultipartFile carImage,
			@RequestPart(value = "rcImage", required = false) MultipartFile rcImage) throws IOException {

		ownerService.createOwnerWithCar(user, carImage, rcImage);
		return ResponseEntity.ok().build();
	}
}
