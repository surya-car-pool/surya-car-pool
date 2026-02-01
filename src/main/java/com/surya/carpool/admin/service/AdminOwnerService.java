package com.surya.carpool.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.surya.carpool.user.model.ApprovalStatus;
import com.surya.carpool.user.model.Role;
import com.surya.carpool.user.model.User;
import com.surya.carpool.user.repository.UserRepository;

@Service
public class AdminOwnerService {

	private final UserRepository userRepository;

	public AdminOwnerService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> getAllOwners() {
		return userRepository.findByRole(Role.OWNER);
	}

	public void approveOwner(Long ownerId) {
		User user = userRepository.findById(ownerId).orElseThrow();
		user.setApprovalStatus(ApprovalStatus.APPROVED);
		user.setEnabled(true);
		userRepository.save(user);
	}

	public void rejectOwner(Long ownerId) {
		User user = userRepository.findById(ownerId).orElseThrow();
		user.setApprovalStatus(ApprovalStatus.REJECTED);
		user.setEnabled(false);
		userRepository.save(user);
	}
}
