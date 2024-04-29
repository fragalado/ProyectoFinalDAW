package com.example.demo.dtos;

import lombok.Data;

/**
 * Clase UsuarioDTO
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
@Data
public class UsuarioDTO {

	// Atributos

	private long id_usuario;
	private String nombre_usuario;
	private String tlf_usuario;
	private String email_usuario;
	private String psswd_usuario;
	private long id_acceso = 1;
	private boolean estaActivado_usuario = false;
	private String imagen_usuario;

	// Constructores -> Constructor vacío

}
