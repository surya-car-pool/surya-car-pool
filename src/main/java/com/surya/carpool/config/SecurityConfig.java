package com.surya.carpool.config;

import com.surya.carpool.repository.UserRepository;
import com.surya.carpool.security.CustomAuthenticationProvider;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationProvider authenticationProvider(UserRepository userRepository,
			PasswordEncoder passwordEncoder) {

		return new CustomAuthenticationProvider(userRepository, passwordEncoder);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider)
			throws Exception {

		http.csrf(csrf -> csrf.disable())

				// -------- AUTHORIZATION --------
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/home", "/login", "/admin/login", "/register", "/css/**", "/js/**",
								"/images/**")
						.permitAll()

						.requestMatchers("/admin/**").hasRole("ADMIN")

						.anyRequest().authenticated())

				// -------- SINGLE LOGIN CONFIG --------
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login").usernameParameter("identifier")
						.passwordParameter("password")

						// ✅ ONLY CHANGE IS HERE
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

				// -------- LOGOUT --------
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/home?logout=true").permitAll())

				.authenticationProvider(authenticationProvider);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

		return configuration.getAuthenticationManager();
	}
}
