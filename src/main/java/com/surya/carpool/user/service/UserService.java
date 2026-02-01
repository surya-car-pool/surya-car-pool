package com.surya.carpool.user.service;

import java.util.List;

import com.surya.carpool.user.model.User;

public interface UserService {

	User createUser(User user);

	User getUserById(Long id);

	List<User> getAllUsers();

}
