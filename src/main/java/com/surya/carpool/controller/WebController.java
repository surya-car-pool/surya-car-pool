package com.surya.carpool.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.surya.carpool.model.Car;
import com.surya.carpool.model.Ride;
import com.surya.carpool.model.User;
import com.surya.carpool.repository.CarRepository;
import com.surya.carpool.repository.RideRepository;
import com.surya.carpool.repository.UserRepository;

@Controller
public class WebController {

	private final RideRepository rideRepository;
	private final UserRepository userRepository;
	private final CarRepository carRepository;

	public WebController(RideRepository rideRepository, UserRepository userRepository, CarRepository carRepository) {
		this.rideRepository = rideRepository;
		this.userRepository = userRepository;
		this.carRepository = carRepository;
	}

	// HOME PAGE – show active cars
	@GetMapping({ "/", "/home" })
	public String home(Model model, @RequestParam(value = "logout", required = false) String logout) {

		List<Car> activeCars = carRepository.findByOwnerEnabledTrue();
		model.addAttribute("cars", activeCars);

		if (logout != null) {
			// for the small banner: th:if="${param.logout}" already in index.html
			model.addAttribute("logout", true);
		}

		return "index"; // templates/index.html
	}

	// USERS UI
	@GetMapping("/users/ui")
	public String usersUi(@RequestParam(value = "id", required = false) Long id, Model model) {

		List<User> users = new ArrayList<>();

		if (id != null) {
			userRepository.findById(id).ifPresent(users::add);
		}

		model.addAttribute("users", users);
		return "users";
	}

	// RIDES UI (search stays as-is, used by the search form in index.html)
	@GetMapping("/rides/ui")
	public String ridesUi(@RequestParam(value = "q", required = false) String q, Model model) {
		List<Ride> rides;
		if (q == null || q.isBlank()) {
			rides = rideRepository.findAll();
		} else {
			rides = rideRepository.findByFromLocationContainingIgnoreCaseOrToLocationContainingIgnoreCase(q, q);
		}

		model.addAttribute("rides", rides);
		return "rides"; // templates/rides.html
	}

	// ABOUT US
	@GetMapping("/aboutus/ui")
	public String aboutUsUi() {
		return "aboutus"; // templates/aboutus.html
	}

	// CONTACT US
	@GetMapping("/contactus/ui")
	public String contactUsUi() {
		return "contactus"; // templates/contactus.html
	}

	// CREATE USER + CAR form UI
	@GetMapping("/createuser/ui")
	public String createUserUi() {
		return "createuser"; // templates/createuser.html
	}

	// LOGIN PAGE
	@GetMapping("/login")
	public String loginPage(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model) {
		if (error != null) {
			model.addAttribute("errorMsg", "Invalid username or password.");
		}
		if (logout != null) {
			model.addAttribute("msg", "You have been logged out successfully.");
		}
		return "login";
	}
	// BOOK SELF-DRIVE CARS PAGE
	@GetMapping("/bookcar/ui")
	public String bookCarUi(Model model) {

	    // Reuse existing logic (same as home page)
	    List<Car> activeCars = carRepository.findByOwnerEnabledTrue();
	    model.addAttribute("cars", activeCars);

	    return "bookcar"; // templates/bookcar.html
	}

}
