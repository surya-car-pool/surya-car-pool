package com.surya.carpool.security;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.surya.carpool.model.User;
import com.surya.carpool.repository.UserRepository;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public CustomAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String identifier = authentication.getName(); // email or phone
		String rawPassword = authentication.getCredentials().toString();

		List<User> users = userRepository.findByEmail(identifier);

		if (users.isEmpty()) {
			users = userRepository.findByPhone(identifier);
		}

		if (users.isEmpty()) {
			throw new BadCredentialsException("Invalid email/phone or password");
		}

		User user = users.get(0);

		if (!user.isEnabled()) {
			throw new BadCredentialsException("User is disabled");
		}

		if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
			throw new BadCredentialsException("Invalid email/phone or password");
		}

		// ✅ FIX: Proper Spring Security role format
		String role = user.getRole(); // USER or ADMIN
		if (role == null || role.isBlank()) {
			role = "USER";
		}

		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

		return new UsernamePasswordAuthenticationToken(user, null, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
