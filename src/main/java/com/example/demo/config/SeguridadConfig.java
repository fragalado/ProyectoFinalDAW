package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * Clase configuración para la autentificación
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
@Configuration
@EnableMethodSecurity
public class SeguridadConfig {

	@Autowired
	private UserDetailsService userDetailsService; // Interfaz para cargar detalles del usuario

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // Encriptador de contraseñas
	}

	@Bean
	public AuthenticationFailureHandler customAuthenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler(); // Fallos personalizados para la autentifiación
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userDetailsService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
				// Cross-Site Request Forgery, método de seguridad que utiliza por defecto
				// spring security
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authRequest -> {
					// Permite acceso a estas url
					authRequest.requestMatchers("/", "/login", "/registro", "/restablecer/**", "/activa-cuenta/**",
							"/css/**", "/js/**", "/img/**").permitAll();
					// Autenticación para cualquier otra solicitud
					authRequest.anyRequest().authenticated();
				}).formLogin(login -> login
						.loginPage("/login") // Establece la página de inicio de sesión personalizada
						.defaultSuccessUrl("/home", true) // Establece la url de redirección después de un inicio
															// exitoso
						.loginProcessingUrl("/login") // Establece la url de procesamiento del formulario de login
						.failureHandler(customAuthenticationFailureHandler()))
				// Configura el cierre de sesión
				.logout(logout -> logout.logoutUrl("/logout") // Establece la url de cierre de sesión personalizada
						.logoutSuccessUrl("/login?logout") // Establece la url de redirección despues de un logout
															// exitoso
				);

		return http.build();
	}
}
