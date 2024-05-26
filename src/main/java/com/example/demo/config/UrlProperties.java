package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase url
 * 
 * Fecha: 24/05/2024
 * @author Francisco José Gallego Dorado
 */
@Data
@NoArgsConstructor
@Configuration
@PropertySource("classpath:datos.properties")
public class UrlProperties {

	// Atributos

	@Value("${miapp.url}")
	private String url;

}
