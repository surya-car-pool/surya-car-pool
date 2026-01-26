package com.surya.carpool.admin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.surya.carpool.user.model.ApprovalStatus;
import com.surya.carpool.user.model.User;
import com.surya.carpool.user.repository.UserRepository;

@RestController
@RequestMapping("/api/admin/owners")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOwnerController {

	private final UserRepository userRepository;

	public AdminOwnerController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@PutMapping("/{id}/approve")
	public void approve(@PathVariable Long id) {
		User u = userRepository.findById(id).orElseThrow();
		u.setApprovalStatus(ApprovalStatus.APPROVED);
		userRepository.save(u);
	}

	@PutMapping("/{id}/reject")
	public void reject(@PathVariable Long id) {
		User u = userRepository.findById(id).orElseThrow();
		u.setApprovalStatus(ApprovalStatus.REJECTED);
		userRepository.save(u);
	}
}
