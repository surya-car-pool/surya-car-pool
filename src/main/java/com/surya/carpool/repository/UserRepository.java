package com.surya.carpool.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.surya.carpool.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByEmail(String email);
	List<User> findByPhone(String phone);
	Optional<User> findById(Long id);


}
