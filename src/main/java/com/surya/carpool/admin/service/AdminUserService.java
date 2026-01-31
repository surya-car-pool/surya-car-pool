package com.surya.carpool.admin.service;

import java.util.List;
import com.surya.carpool.user.model.User;

public interface AdminUserService {

	List<User> getAllUsers();

	void blockUser(Long userId);

	void unblockUser(Long userId);
}
