package com.surya.carpool.controller;

import com.surya.carpool.model.User;
import com.surya.carpool.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(
            @RequestParam("regName") String regName,
            @RequestParam("regEmail") String regEmail,
            @RequestParam("regPhone") String regPhone,
            @RequestParam("regPassword") String regPassword
    ) {
        // TODO: add validation + duplicate checks (email/phone)
        User user = new User();
        user.setName(regName);
        user.setEmail(regEmail);
        user.setPhone(regPhone);
        user.setPassword(passwordEncoder.encode(regPassword)); // hash password
        user.setEnabled(true);

        userRepository.save(user);

        // Redirect to login page
        return "redirect:/login?registered=true";
    }
}
