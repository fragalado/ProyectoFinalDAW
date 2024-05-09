package com.example.demo.servicios;

import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.example.demo.daos.Token;
import com.example.demo.daos.Usuario;
import com.example.demo.dtos.UsuarioDTO;
import com.example.demo.repositorios.UsuarioRepository;
import com.example.demo.utiles.Util;

import jakarta.transaction.Transactional;

/**
 * Implementación de la interfaz Usuario
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
@Service
@Transactional
public class UsuarioImplementacion implements UsuarioInterfaz {

	@Autowired
	private UsuarioRepository usuarioRepositorio;

	@Autowired
	private EmailImplementacion emailImplementacion;

	@Autowired
	private TokenImplementacion tokenImplementacion;

	@Override
	public List<UsuarioDTO> obtieneTodosLosUsuarios() {
		try {
			// Obtenemos todos los usuarios de la base de datos y lo guardamos en una lista
			// de tipo Usuario (DAO)
			List<Usuario> listaUsuariosDao = usuarioRepositorio.findAll();

			// Pasamos de DAO a DTO
			List<UsuarioDTO> listaUsuariosDTO = Util.listaUsuarioDaoADto(listaUsuariosDao);

			// Devolvemos la lista de usuarios DTO
			return listaUsuariosDTO;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public UsuarioDTO obtieneUsuarioPorId(long id_usuario) {
		try {
			// Obtenemos el usuario
			Optional<Usuario> usuarioEncontrado = usuarioRepositorio.findById(id_usuario);

			// Comprobamos si no se ha encontrado el usuario
			if (usuarioEncontrado.isEmpty())
				return null;

			// Si se ha encontrado vamos a convertir el usuario de DAO a DTO
			UsuarioDTO usuarioDTO = Util.usuarioADto(usuarioEncontrado.get());

			// Devolvemos el usuario convertido a DTO
			return usuarioDTO;
		} catch (IllegalArgumentException e) {
			return null;
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public UsuarioDTO obtieneUsuarioPorEmail(String email) {
		try {
			// Buscamos el usuario por el email
			Usuario usuarioEncontrado = usuarioRepositorio.findByEmailUsuario(email);

			// Convertimos el usuario a DTO y lo devolvemos
			return Util.usuarioADto(usuarioEncontrado);
		} catch (Exception e) {
			return null; // Devuelve null en caso de no encontrarlo
		}
	}

	@Override
	public Boolean registrarUsuario(UsuarioDTO usuario) {

		try {
			// Buscamos si existe un usuario con el email introducido
			UsuarioDTO usuarioEncontrado = obtieneUsuarioPorEmail(usuario.getEmail_usuario());

			if (usuarioEncontrado != null) {
				// Se ha encontrado un usuario con el email introducido
				// Luego devolveremos false
				return false;
			}

			// Si no se ha encontrado seguimos con el registro
			// Convertimos el usuario (DTO) a DAO
			Usuario usuarioDAO = Util.usuarioADao(usuario);

			// Lo guardamos en la base de datos
			Usuario usuarioDevuelto = usuarioRepositorio.save(usuarioDAO);

			// Si el Usuario devuelto es distinto de null es porque se ha registrado
			// correctamente
			if (usuarioDevuelto != null) {
				// Enviamos el correo
				emailImplementacion.enviarEmail("http://localhost:8080/activa-cuenta", true, usuarioDevuelto);
			}
			return usuarioDevuelto != null;
		} catch (IllegalArgumentException e) {
			return null;
		} catch (OptimisticLockingFailureException e) {
			// Excepcion de concurrencia optimista
			// Esto puede ocurrir si otro proceso ha modificado los datos mientras
			// esta transacción estaba realizando sus operaciones.
			return null;
		}
	}

	@Override
	public boolean activaCuenta(String token, String email) {
		try {
			// Obtenemos el token de la base de datos
			Token tokenDao = tokenImplementacion.obtieneToken(token);

			// Ahora comprobamos si el token no ha caducado
			// Obtenemos la fecha actual
			Calendar fechaActual = Calendar.getInstance();

			// Comparamos la fechaActual con la fecha del token
			if (fechaActual.compareTo(tokenDao.getFch_fin_token()) < 0
					|| fechaActual.compareTo(tokenDao.getFch_fin_token()) == 0) {
				// La fecha actual es menor que la fecha del token o son iguales, luego seguimos
				// con el proceso
				// Obtenemos el usuario del token
				Usuario usuarioDao = tokenDao.getUsuario();

				// Comprobamos si el email introducido es distinto al email del usuario
				if (!email.equals(usuarioDao.getEmail_usuario())) {
					return false;
				}

				// Modificamos la propiedad estaActivado y la ponemos en true
				usuarioDao.setEstaActivado_usuario(true);

				// Actualizamos en la base de datos
				Usuario usuarioDevuelto = usuarioRepositorio.save(usuarioDao);

				// Comprobamos si se ha actualizado correctamente
				if (usuarioDevuelto != null && usuarioDevuelto.isEstaActivado_usuario()) {
					return true; // El usuario ha sido activado
				} else {
					return false; // El usuario no ha sido activado
				}
			} else {
				// La fecha actual es mayor que la fecha del token, luego ha caducado
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (OptimisticLockingFailureException e) {
			return false;
		}
	}

	@Override
	public Boolean peticionCambiaPassword(String email) {
		try {
			// Obtenemos el usuario de la base de datos por el email
			Usuario usuarioEncontrado = Util.usuarioADao(obtieneUsuarioPorEmail(email));

			// Si usuarioEncontrado es null devolvemos false
			if (usuarioEncontrado == null)
				return false; // El email introducido no existe

			// Si llega aqui es porque se ha encontrado el usuario.
			// Luego enviamos un email
			boolean ok = emailImplementacion.enviarEmail("http://localhost:8080/restablecer/cambiar-password", false,
					usuarioEncontrado);

			// Controlamos la respuesta
			if (ok)
				return true; // Se ha enviado el correo correctamente
			else
				return null; // Se ha producido un error al enviar el correo
		} catch (Exception e) {
			return null; // Se ha producido un error al enviar el correo
		}
	}

	@Override
	public boolean modificaPassword(String token, String password) {
		try {
			// Obtenemos el token
			Token tokenDao = tokenImplementacion.obtieneToken(token);

			// Ahora comprobamos si el token no ha caducado
			// Obtenemos la fecha actual
			Calendar fechaActual = Calendar.getInstance();

			// Comparamos la fechaActual con la fecha del token
			if (fechaActual.compareTo(tokenDao.getFch_fin_token()) < 0
					|| fechaActual.compareTo(tokenDao.getFch_fin_token()) == 0) {
				// La fecha actual es menor que la fecha del token o son iguales, luego seguimos
				// con el proceso
				// Obtenemos el usuario
				Usuario usuarioDao = tokenDao.getUsuario();

				// Modificamos la contraseña
				usuarioDao.setPsswd_usuario(password);

				// Actualizamos en la base de datos
				usuarioRepositorio.save(usuarioDao);

				return true;
			} else {
				// La fecha actual es mayor que la fecha del token, luego ha caducado
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (OptimisticLockingFailureException e) {
			return false;
		}
	}

	@Override
	public boolean borraUsuarioPorId(long id_usuario) {
		try {
			// Comprobamos si existe un usuario con el id pasado
			UsuarioDTO usuarioEncontrado = obtieneUsuarioPorId(id_usuario);

			// Comprobamos si no se ha encontrado
			if (usuarioEncontrado == null)
				return false;

			// Si existe comprobamos que no sea administrador
			if(usuarioEncontrado.getId_acceso() == 2)
				return false;
			
			// Si no es admin lo eliminamos
			usuarioRepositorio.deleteById(id_usuario);

			// Ahora para comprobar si se ha eliminado vamos a buscar el usuario por el id
			UsuarioDTO usuarioDTO = obtieneUsuarioPorId(id_usuario);

			if (usuarioDTO == null)
				return true; // Devolvemos true si no existe

			return false; // En caso de que se haya encontrado un usuario con el id
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	@Override
	public boolean actualizaUsuario(UsuarioDTO usuarioDTO) {
		try {
			// Con el id del usuario pasado obtenemos el usuario de la base de datos
			Usuario usuarioEncontrado = Util.usuarioADao(obtieneUsuarioPorId(usuarioDTO.getId_usuario()));
			// Optional<Usuario> usuarioEncontrado =
			// usuarioRepositorio.findById(usuarioDTO.getId_usuario());

			// Actualizamos algunos datos del usuarioEncontrado con el usuarioDTO
			usuarioEncontrado.setNombre_usuario(usuarioDTO.getNombre_usuario());
			usuarioEncontrado.setEmail_usuario(usuarioDTO.getEmail_usuario());
			usuarioEncontrado.setTlf_usuario(usuarioDTO.getTlf_usuario());
			if (usuarioDTO.getImagen_usuario() != null)
				usuarioEncontrado.setImagen_usuario(Util.convertirAByteArray(usuarioDTO.getImagen_usuario()));

			// Actualizamos el usuario
			usuarioRepositorio.save(usuarioEncontrado);

			return true;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (OptimisticLockingFailureException e) {
			return false;
		}
	}

	@Override
	public boolean agregaUsuario(UsuarioDTO usuarioDTO) {
		try {
			// Buscamos si existe un usuario con el email introducido
			UsuarioDTO usuarioEncontrado = obtieneUsuarioPorEmail(usuarioDTO.getEmail_usuario());

			if (usuarioEncontrado != null) {
				// Se ha encontrado un usuario con el email introducido
				// Luego devolveremos false
				return false;
			}

			// Si no se ha encontrado
			// Activamos la cuenta
			usuarioDTO.setEstaActivado_usuario(true);

			// Guardamos el usuario
			usuarioRepositorio.save(Util.usuarioADao(usuarioDTO));

			return true;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (OptimisticLockingFailureException e) {
			return false;
		}
	}

	@Override
	public boolean editarPerfil(UsuarioDTO usuarioActual, UsuarioDTO usuarioNuevo) {
		try {
			// Cambiamos los valores del usuarioActual
			usuarioActual.setNombre_usuario(usuarioNuevo.getNombre_usuario());
			usuarioActual.setTlf_usuario(usuarioNuevo.getTlf_usuario());
			if (usuarioNuevo.getImagen_usuario() != null)
				usuarioActual.setImagen_usuario(usuarioNuevo.getImagen_usuario());

			// Convertimos a DAO el usuarioActual
			Usuario usuarioDao = Util.usuarioADao(usuarioActual);

			// Actualizamos
			Usuario usuarioDevuelto = usuarioRepositorio.save(usuarioDao);

			if (usuarioDevuelto.getNombre_usuario().equals(usuarioNuevo.getNombre_usuario())
					&& usuarioDevuelto.getTlf_usuario().equals(usuarioNuevo.getTlf_usuario()))
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

}
