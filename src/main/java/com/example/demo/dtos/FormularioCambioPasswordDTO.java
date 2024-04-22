package com.example.demo.dtos;

import lombok.Data;

/**
 * Clase FormularioCambioPasswordDTO, clase que sirve para el formulario para cambiar contraseñas
 * 
 * @author Francisco José Gallego Dorado
 * Fecha: 22/04/2024
 */
@Data
public class FormularioCambioPasswordDTO {

	// Atributos
	
	private String password1;
	private String password2;
}
