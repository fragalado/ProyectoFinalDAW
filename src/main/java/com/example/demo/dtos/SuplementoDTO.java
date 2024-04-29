package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase SuplementoDTO
 * 
 * @author Francisco José Gallego Dorado
 * Fecha: 27/04/2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuplementoDTO {

	// Atributos
	
	private long id_suplemento;
	private String nombre_suplemento;
	private String desc_suplemento;
	private float precio_suplemento;
	private String tipo_suplemento;
	private String marca_suplemento;
	private String imagen_suplemento;
}
