package com.surya.carpool.controller;

import com.surya.carpool.model.User;
import com.surya.carpool.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserRepository repo;
	private final PasswordEncoder passwordEncoder;

	public UserController(UserRepository repo, PasswordEncoder passwordEncoder) {
		this.repo = repo;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping
	public ResponseEntity<?> createUser(@RequestBody User user) {
	    Map<String, String> body = new HashMap<>();

	    try {
	        if (user.getName() == null || user.getName().isBlank()
	                || user.getEmail() == null || user.getEmail().isBlank()
	                || user.getPhone() == null || user.getPhone().isBlank()
	                || user.getPassword() == null || user.getPassword().isBlank()) {

	            body.put("message", "Please fill all mandatory fields: name, email, phone, password.");
	            return ResponseEntity.badRequest().body(body);
	        }

	        // basic validation for at least one car (for owner attaching car)
	        if (user.getCars() == null || user.getCars().isEmpty()) {
	            body.put("message", "Please add at least one car for this owner.");
	            return ResponseEntity.badRequest().body(body);
	        }

	        // attach owner to each car
	        if (user.getCars() != null) {
	            user.getCars().forEach(c -> c.setOwner(user));
	        }

	        // encode password
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        user.setEnabled(true);

	        User saved = repo.save(user); // thanks to cascade, cars are saved too
	        return ResponseEntity.ok(saved);

	    } catch (DataIntegrityViolationException ex) {
	        body.put("message", "Email, phone or car registration already exists.");
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        body.put("message", "Unexpected error while saving owner with cars.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	    }
	}


	@GetMapping
	public List<User> listUsers() {
		return repo.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable Long id) {
		return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// 🔥 NEW: delete user by id with FK safety
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) {
		Map<String, String> body = new HashMap<>();

		if (!repo.existsById(id)) {
			body.put("message", "User not found with id " + id);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
		}

		try {
			repo.deleteById(id);
			// success
			return ResponseEntity.noContent().build(); // 204
		} catch (DataIntegrityViolationException ex) {
			// This is your "Cannot delete or update a parent row" case
			body.put("message", "Cannot delete this user because there are bookings linked to them. "
					+ "Please cancel or reassign those bookings first.");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
		} catch (Exception ex) {
			ex.printStackTrace();
			body.put("message", "Unexpected error while deleting user. Please try again.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
		}
	}

}
