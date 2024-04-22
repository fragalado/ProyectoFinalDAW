package com.example.demo.servicios;

import com.example.demo.daos.Token;

/**
 * Interfaz que define los métodos que darán servicio a Token
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
public interface TokenInterfaz {

	/**
	 * Método que guarda un token en la base de datos
	 * 
	 * @param token El token (dao) que se va a guardar
	 * @return Devuelve true si se ha guardado o false si no se ha guardado
	 */
	public boolean guardaToken(Token token);

	/**
	 * Método que obtiene un token de la base de datos y lo devuelve.
	 * 
	 * @param token Código del token a ser buscado
	 * @return Devuelve el token encontrado
	 */
	public Token obtieneToken(String token);
}
