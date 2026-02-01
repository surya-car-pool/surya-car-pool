package com.surya.carpool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.surya.carpool.auth.security.CustomAuthenticationProvider;
import com.surya.carpool.user.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	// =====================
	// Password Encoder
	// =====================
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// =====================
	// Custom Authentication Provider
	// =====================
	@Bean
	public AuthenticationProvider authenticationProvider(UserRepository userRepository,
			PasswordEncoder passwordEncoder) {

		return new CustomAuthenticationProvider(userRepository, passwordEncoder);
	}

	// =====================
	// Security Filter Chain
	// =====================
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider)
			throws Exception {

		http
				// ---- CSRF ----
				.csrf(csrf -> csrf.disable())

				// ---- Authorization ----
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/home", "/login", "/admin/login", "/register", "/css/**", "/js/**",
								"/images/**", "/static/**")
						.permitAll()

						.requestMatchers("/admin/**").hasRole("ADMIN")

						.anyRequest().authenticated())

				// ---- Login ----
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login").usernameParameter("identifier")
						.passwordParameter("password")

						.successHandler((request, response, authentication) -> {

							boolean isAdmin = authentication.getAuthorities().stream()
									.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

							if (isAdmin) {
								response.sendRedirect("/admin/dashboard");
							} else {
								response.sendRedirect("/home");
							}
						})

						.failureUrl("/login?error=true").permitAll())

				// ---- Logout ----
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/home?logout=true").permitAll())

				// ---- Provider ----
				.authenticationProvider(authenticationProvider);

		return http.build();
	}

	// =====================
	// Authentication Manager
	// =====================
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

		return configuration.getAuthenticationManager();
	}
}
