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

	// Password encoder for registration + login
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Our custom AuthenticationProvider: identifier (email/phone) + password
	@Bean
	public AuthenticationProvider authenticationProvider(UserRepository userRepository,
			PasswordEncoder passwordEncoder) {
		return new CustomAuthenticationProvider(userRepository, passwordEncoder);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider)
			throws Exception {

		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
				// Public endpoints
				.requestMatchers("/", // landing/root
						"/home", // home is public so user can see it after logout
						"/login", // login page + POST /login
						"/register", // registration POST
						"/css/**", "/js/**", "/images/**")
				.permitAll()
				// Everything else requires login (including /users/ui, /rides/ui, etc.)
				.anyRequest().authenticated()).formLogin(form -> form.loginPage("/login") // GET /login -> login.html
						.loginProcessingUrl("/login") // POST /login
						.usernameParameter("identifier") // from login.html
						.passwordParameter("password") // from login.html
						.defaultSuccessUrl("/home", true) // on success
						.failureUrl("/login?error=true") // on failure -> friendly error block
						.permitAll())
				.logout(logout -> logout.logoutUrl("/logout")
						// after logout go to home (you can show a message using param.logout on
						// home.html)
						.logoutSuccessUrl("/home?logout=true").permitAll())
				.authenticationProvider(authenticationProvider);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}
