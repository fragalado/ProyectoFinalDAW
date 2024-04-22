package com.example.demo.servicios;

import com.example.demo.dtos.UsuarioDTO;

/**
 * Interfaz que define los métodos que darán servicio a un usuario
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
public interface UsuarioInterfaz {

	/**
	 * Método que obtiene un usuario de la base de datos por el email
	 * 
	 * @param email Email del usuario a buscar
	 * @return Devuelve el usuario encontrado o null en caso de no encontrarlo
	 */
	public UsuarioDTO obtieneUsuarioPorEmail(String email);

	/**
	 * Método que hace el registro de un usuario a la base de datos y envía un
	 * correo para confirmar cuenta. Si el email introducido ya existe no hará el
	 * registro.
	 * 
	 * @param usuario Objeto usuario con los datos.
	 * @return Devuelve true si se ha producido el registro ,false si el email ya
	 *         existe o null si se ha producido un error.
	 */
	public Boolean registrarUsuario(UsuarioDTO usuario);

	/**
	 * Método que activa la cuenta de un usuario. Se le pasa por parámetros el
	 * token.
	 * 
	 * @param token El token.
	 * @param email Email del usuario a activar cuenta.
	 * @return Devuelve true si se ha activado la cuenta o false si no.
	 */
	public boolean activaCuenta(String token, String email);

	/**
	 * Método que cambia la contraseña de un usuario
	 * 
	 * @param token      Token
	 * @param password Contraseña encriptada
	 * @return Devuelte true si se ha cambiado las contraseñas con éxito o false si
	 *         ha ocurrido un error.
	 */
	public boolean modificaPassword(String token, String password);

	/**
	 * Método que obtiene un usuario de la base de datos, crea un token, lo guarda
	 * en la base de datos el token y envía un correo al email del usuario.
	 * 
	 * @param email Email del usuario a cambiar password
	 * @return Devuelve true si se ha enviado el correo, false si no se ha
	 *         encontrado el email en la base de datos o null si se ha producido un
	 *         error.
	 */
	public Boolean peticionCambiaPassword(String email);
}
