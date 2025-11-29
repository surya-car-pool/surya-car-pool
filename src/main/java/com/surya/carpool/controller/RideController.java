package com.surya.carpool.controller;

import com.surya.carpool.model.Ride;
import com.surya.carpool.model.User;
import com.surya.carpool.repository.RideRepository;
import com.surya.carpool.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController {
    private final RideRepository rideRepo;
    private final UserRepository userRepo;

    public RideController(RideRepository rideRepo, UserRepository userRepo) {
        this.rideRepo = rideRepo;
        this.userRepo = userRepo;
    }

    @PostMapping
    public ResponseEntity<?> createRide(@RequestBody Ride ride) {
        if (ride.getDriver() != null && ride.getDriver().getId() != null) {
            User driver = userRepo.findById(ride.getDriver().getId()).orElse(null);
            if (driver == null) return ResponseEntity.badRequest().body("Driver not found");
            ride.setDriver(driver);
        }
        if (ride.getDepartureTime() == null) {
            ride.setDepartureTime(LocalDateTime.now().plusDays(1));
        }
        Ride saved = rideRepo.save(ride);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
	public List<Ride> listRides(@RequestParam(required = false) String q) {
        if (q == null || q.isBlank()) return rideRepo.findAll();
        return rideRepo.findByFromLocationContainingIgnoreCaseOrToLocationContainingIgnoreCase(q, q);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ride> getRide(@PathVariable Long id) {
        return rideRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
