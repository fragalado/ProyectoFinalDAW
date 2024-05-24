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

	private long idUsuario;
	private String nombreUsuario;
	private String tlfUsuario;
	private String emailUsuario;
	private String psswdUsuario;
	private long idAcceso = 1;
	private boolean estaActivadoUsuario = false;
	private String imagenUsuario;

	// Constructores -> Constructor vacío

}
