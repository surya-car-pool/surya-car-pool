package com.surya.carpool.config;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
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

	@Bean
	public RequestCache savedRequestCache() {
		return new HttpSessionRequestCache();
	}

	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return username -> userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	// =====================
	// Security Filter Chain
	// =====================
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider,
			RequestCache savedRequestCache, UserDetailsService userDetailsService,
			@Value("${surya.security.remember-me-key:}") String configuredRememberMeKey)
			throws Exception {
		final String rememberMeKey;
		if (configuredRememberMeKey.isBlank()) {
			byte[] key = new byte[32];
			new SecureRandom().nextBytes(key);
			rememberMeKey = Base64.getEncoder().encodeToString(key);
		} else {
			rememberMeKey = configuredRememberMeKey;
		}
		SavedRequestAwareAuthenticationSuccessHandler userSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		userSuccessHandler.setRequestCache(savedRequestCache);
		userSuccessHandler.setDefaultTargetUrl("/home");

		http
				// ---- CSRF ----
				.csrf(csrf -> csrf.disable())

				// ---- Authorization ----
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/home", "/login", "/admin/login", "/register", "/css/**", "/js/**",
								"/images/**", "/static/**", "/aboutus/**", "/contactus/**", "/customer-app",
								"/customer-app/**", "/owner-app", "/owner-app/**", "/terms", "/privacy", "/faq")
						.permitAll()

						.requestMatchers("/admin/**").hasRole("ADMIN")

						.anyRequest().authenticated())

				.requestCache(cache -> cache.requestCache(savedRequestCache))

				// ---- Login ----
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login").usernameParameter("identifier")
						.passwordParameter("password")

						.successHandler((request, response, authentication) -> {

							boolean isAdmin = authentication.getAuthorities().stream()
									.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

							if (isAdmin) {
								response.sendRedirect("/admin/dashboard");
							} else {
								userSuccessHandler.onAuthenticationSuccess(request, response, authentication);
							}
						})

						.failureUrl("/login?error=true").permitAll())

				.rememberMe(remember -> remember.key(rememberMeKey)
						.rememberMeParameter("remember-me")
						.userDetailsService(userDetailsService)
						.tokenValiditySeconds(14 * 24 * 60 * 60))

				// ---- Logout ----
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/home?logout=true")
						.deleteCookies("remember-me").permitAll())

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
