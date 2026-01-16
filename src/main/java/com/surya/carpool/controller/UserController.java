package com.surya.carpool.controller;

import com.surya.carpool.model.User;
import com.surya.carpool.model.Car;
import com.surya.carpool.repository.UserRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createUser(
            @RequestPart("user") User user,
            @RequestPart(value = "carImage", required = false) MultipartFile carImage,
            @RequestPart(value = "rcImage", required = false) MultipartFile rcImage
    ) {
        try {

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            if (user.getCars() != null && !user.getCars().isEmpty()) {

                Car car = user.getCars().get(0);

                String uploadDir = "uploads/cars/";
                Files.createDirectories(Paths.get(uploadDir));

                if (carImage != null && !carImage.isEmpty()) {
                    String fileName = System.currentTimeMillis() + "_car_" + carImage.getOriginalFilename();
                    Path path = Paths.get(uploadDir + fileName);
                    Files.copy(carImage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    car.setCarImagePath("/" + uploadDir + fileName);
                }

                if (rcImage != null && !rcImage.isEmpty()) {
                    String fileName = System.currentTimeMillis() + "_rc_" + rcImage.getOriginalFilename();
                    Path path = Paths.get(uploadDir + fileName);
                    Files.copy(rcImage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    car.setRcImagePath("/" + uploadDir + fileName);
                }
            }

            User saved = repo.save(user);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();

            Map<String, String> error = new HashMap<>();
            error.put("message", "Failed to save owner & car");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Map<String, String> body = new HashMap<>();

        if (!repo.existsById(id)) {
            body.put("message", "User not found with id " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
        }

        try {
            repo.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException ex) {
            body.put("message", "Cannot delete this user because there are bookings linked to them.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        } catch (Exception ex) {
            ex.printStackTrace();
            body.put("message", "Unexpected error while deleting user.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
        }
    }
}
