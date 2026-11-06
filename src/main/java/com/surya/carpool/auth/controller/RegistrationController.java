package com.surya.carpool.auth.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.surya.carpool.auth.dto.RegisterRequest;
import com.surya.carpool.user.model.Role;
import com.surya.carpool.user.model.User;
import com.surya.carpool.user.repository.UserRepository;

@Controller
public class RegistrationController {

	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
	private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{7,15}$");
	private static final Pattern PINCODE_PATTERN = Pattern.compile("^[0-9]{6}$");
	private static final Pattern STRONG_PASSWORD_PATTERN = Pattern.compile(
			"^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{12,}$");
	private static final Set<String> GENDERS = Set.of("MALE", "FEMALE", "NON_BINARY");

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/register")
	public String register(@ModelAttribute RegisterRequest request) {
		String name = clean(request.getRegName());
		String email = clean(request.getRegEmail()).toLowerCase(Locale.ROOT);
		String phone = clean(request.getRegPhone());
		String city = clean(request.getRegCity());
		String state = clean(request.getRegState());
		String pincode = clean(request.getRegPincode());
		String gender = clean(request.getRegGender()).toUpperCase(Locale.ROOT);
		LocalDate dateOfBirth = parseDate(request.getRegDateOfBirth());

		if (!isValid(name, email, phone, request.getRegPassword(), request.getRegConfirmPassword(),
				city, state, pincode, gender, request.isTermsAccepted(), request.isPrivacyAccepted(),
				request.getRegDateOfBirth(), dateOfBirth)) {
			return "redirect:/login?registerError=validation";
		}

		if (userRepository.existsByEmailIgnoreCase(email)) {
			return "redirect:/login?registerError=email";
		}
		if (userRepository.existsByPhone(phone)) {
			return "redirect:/login?registerError=phone";
		}

		LocalDateTime acceptedAt = LocalDateTime.now();
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPhone(phone);
		user.setPassword(passwordEncoder.encode(request.getRegPassword()));
		user.setRole(Role.USER);
		user.setEnabled(true);
		user.setCity(city);
		user.setState(state);
		user.setPincode(pincode);
		user.setGender(gender.isEmpty() ? null : gender);
		user.setDateOfBirth(dateOfBirth);
		user.setTermsAcceptedAt(acceptedAt);
		user.setPrivacyAcceptedAt(acceptedAt);

		userRepository.save(user);
		return "redirect:/login?registered=true";
	}

	private boolean isValid(String name, String email, String phone, String password, String confirmPassword,
			String city, String state, String pincode, String gender, boolean termsAccepted, boolean privacyAccepted,
			String dateOfBirthInput, LocalDate dateOfBirth) {
		if (name.isEmpty() || name.length() > 100 || !EMAIL_PATTERN.matcher(email).matches()
				|| email.length() > 120 || !PHONE_PATTERN.matcher(phone).matches()
				|| password == null || !STRONG_PASSWORD_PATTERN.matcher(password).matches()
				|| !password.equals(confirmPassword) || city.isEmpty() || city.length() > 100
				|| state.isEmpty() || state.length() > 100 || !PINCODE_PATTERN.matcher(pincode).matches()
				|| (!gender.isEmpty() && !GENDERS.contains(gender))
				|| !termsAccepted || !privacyAccepted) {
			return false;
		}
		return dateOfBirthInput == null || dateOfBirthInput.isBlank()
				|| (dateOfBirth != null && !dateOfBirth.isAfter(LocalDate.now()));
	}

	private LocalDate parseDate(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		try {
			return LocalDate.parse(value);
		} catch (DateTimeParseException exception) {
			return null;
		}
	}

	private String clean(String value) {
		return value == null ? "" : value.trim();
	}
}
