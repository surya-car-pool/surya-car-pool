package com.surya.carpool.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.surya.carpool.user.model.ApprovalStatus;
import com.surya.carpool.user.model.Role;
import com.surya.carpool.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	// Find by email (used for login)
	Optional<User> findByEmail(String email);

	// Find by phone (used for login)
	List<User> findByPhone(String phone);

	// Check duplicates during registration
	boolean existsByEmail(String email);

	boolean existsByPhone(String phone);
	
	List<User> findByRoleAndApprovalStatus(Role role, ApprovalStatus status);

    List<User> findByRole(Role role);
}
