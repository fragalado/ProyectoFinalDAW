package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase configuración email
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 29/04/2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@PropertySource("classpath:datos.properties")
public class EmailProperties {

	// Atributos

	@Value("${miapp.emailFrom}")
	private String emailFrom;
}
