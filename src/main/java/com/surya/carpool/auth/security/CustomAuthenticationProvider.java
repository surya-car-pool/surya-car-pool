package com.surya.carpool.auth.security;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.surya.carpool.user.model.Role;
import com.surya.carpool.user.model.User;
import com.surya.carpool.user.repository.UserRepository;

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

		User user = null;

		// 1. Try login by email
		Optional<User> byEmail = userRepository.findByEmail(identifier);
		if (byEmail.isPresent()) {
			user = byEmail.get();
		} else {
			// 2. Try login by phone
			List<User> usersByPhone = userRepository.findByPhone(identifier);
			if (!usersByPhone.isEmpty()) {
				user = usersByPhone.get(0);
			}
		}

		// 3. If still not found → invalid login
		if (user == null) {
			throw new BadCredentialsException("Invalid email/phone or password");
		}

		// 4. Check enabled
		if (!user.isEnabled()) {
			throw new BadCredentialsException("User is disabled");
		}

		// 5. Check password
		if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
			throw new BadCredentialsException("Invalid email/phone or password");
		}

		// 6. Role handling (FIXED)
		Role userRole = user.getRole();
		String roleName = (userRole != null) ? userRole.name() : "USER";

		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + roleName));

		// 7. Return authenticated token
		return new UsernamePasswordAuthenticationToken(user, null, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
