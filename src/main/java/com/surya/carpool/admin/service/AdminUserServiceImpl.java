package com.surya.carpool.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.surya.carpool.user.model.User;
import com.surya.carpool.user.repository.UserRepository;

@Service
@Transactional
public class AdminUserServiceImpl implements AdminUserService {

	private final UserRepository userRepository;

	public AdminUserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public void blockUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		user.setEnabled(false);
		userRepository.save(user);
	}

	@Override
	public void unblockUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		user.setEnabled(true);
		userRepository.save(user);
	}

}
