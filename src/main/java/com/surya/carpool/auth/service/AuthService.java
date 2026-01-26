package com.surya.carpool.auth.service;

import com.surya.carpool.user.model.User;

public interface AuthService {

	User login(String username, String password);

}
