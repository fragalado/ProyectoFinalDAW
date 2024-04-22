package com.example.demo.dtos;

import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase TokenDTO
 * 
 * @author Francisco José Gallego Dorado
 * Fecha: 21/04/2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {

	// Atributos
	
	private long id_token;
	private String cod_token;
	private Calendar fch_fin_token;
	private long id_usuario;
}
