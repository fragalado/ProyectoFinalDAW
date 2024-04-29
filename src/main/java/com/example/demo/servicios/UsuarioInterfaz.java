package com.example.demo.servicios;

import java.util.List;

import com.example.demo.dtos.UsuarioDTO;

/**
 * Interfaz que define los métodos que darán servicio a un usuario
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
public interface UsuarioInterfaz {

	/**
	 * Método que obtiene todos los usuarios de la base de datos
	 * 
	 * Fecha: 27/04/2024
	 * 
	 * @return Devuelve una lista de objetos UsuarioDTO
	 */
	public List<UsuarioDTO> obtieneTodosLosUsuarios();

	/**
	 * Método que obtiene un usuario por el id
	 * 
	 * Fecha: 27/04/2024
	 * 
	 * @param id_usuario Id del usuario a obtener
	 * @return Devuelve un objeto UsuarioDTO
	 */
	public UsuarioDTO obtieneUsuarioPorId(long id_usuario);

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
	 * @param token    Token
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

	/**
	 * Método que elimina un usuario por su id.
	 * 
	 * Fecha: 27/04/2024
	 * 
	 * @param id_usuario Id del usuario a eliminar
	 * @return Devuelve true si se ha eliminado o false si no.
	 */
	public boolean borraUsuarioPorId(long id_usuario);

	/**
	 * Método que actualiza un usuario.
	 * 
	 * Fecha: 27/04/2024
	 * 
	 * @param usuarioDTO Objeto UsuarioDTO con los nuevos datos del usuario.
	 * @return Devuelve true si se ha actualizado o false si no.
	 */
	public boolean actualizaUsuario(UsuarioDTO usuarioDTO);

	/**
	 * Método que agrega un nuevo usuario a la base de datos.
	 * 
	 * Fecha: 27/04/2024
	 * 
	 * @param usuarioDTO Objeto UsuarioDTO con los datos del nuevo usuario.
	 * @return Devuelve true si se ha agregado o false si no.
	 */
	public boolean agregaUsuario(UsuarioDTO usuarioDTO);
}
