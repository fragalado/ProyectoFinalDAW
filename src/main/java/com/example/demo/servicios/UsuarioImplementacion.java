package com.example.demo.servicios;

import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.example.demo.config.UrlProperties;
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

	@Autowired
	private UrlProperties url;

	@Override
	public List<UsuarioDTO> obtieneTodosLosUsuarios() {
		try {
			// Log
			Util.logInfo("UsuarioImplementacion", "obtieneTodosLosUsuarios", "Ha entrado");

			// Obtenemos todos los usuarios de la base de datos y lo guardamos en una lista
			// de tipo Usuario (DAO)
			List<Usuario> listaUsuariosDao = usuarioRepositorio.findAll();

			// Pasamos de DAO a DTO
			List<UsuarioDTO> listaUsuariosDTO = Util.listaUsuarioDaoADto(listaUsuariosDao);

			// Devolvemos la lista de usuarios DTO
			return listaUsuariosDTO;
		} catch (Exception e) {
			// Log
			Util.logError("UsuarioImplementacion", "obtieneTodosLosUsuarios", "Se ha producido un error.");

			return null;
		}
	}

	@Override
	public UsuarioDTO obtieneUsuarioPorId(long idUsuario) {
		try {
			// Log
			Util.logInfo("UsuarioImplementacion", "obtieneUsuarioPorId", "Ha entrado");

			// Obtenemos el usuario
			Optional<Usuario> usuarioEncontrado = usuarioRepositorio.findById(idUsuario);

			// Comprobamos si no se ha encontrado el usuario
			if (usuarioEncontrado.isEmpty())
				return null;

			// Si se ha encontrado vamos a convertir el usuario de DAO a DTO
			UsuarioDTO usuarioDTO = Util.usuarioADto(usuarioEncontrado.get());

			// Devolvemos el usuario convertido a DTO
			return usuarioDTO;
		} catch (IllegalArgumentException e) {
			// Log
			Util.logError("UsuarioImplementacion", "obtieneUsuarioPorId",
					"Se ha intentado pasar a un método un argumento ilegal o inapropiado.");

			return null;
		} catch (NoSuchElementException e) {
			// Log
			Util.logError("UsuarioImplementacion", "obtieneUsuarioPorId",
					"Se ha intentado pasar a un método un argumento que no existe.");

			return null;
		}
	}

	@Override
	public UsuarioDTO obtieneUsuarioPorEmail(String email) {
		try {
			// Log
			Util.logInfo("UsuarioImplementacion", "obtieneUsuarioPorEmail", "Ha entrado");

			// Buscamos el usuario por el email
			Usuario usuarioEncontrado = usuarioRepositorio.findByEmailUsuario(email);

			// Convertimos el usuario a DTO y lo devolvemos
			if (usuarioEncontrado != null)
				return Util.usuarioADto(usuarioEncontrado);
			return null;
		} catch (Exception e) {
			// Log
			Util.logError("UsuarioImplementacion", "obtieneUsuarioPorEmail", "Se ha producido un error.");

			return null; // Devuelve null en caso de no encontrarlo
		}
	}

	@Override
	public Boolean registrarUsuario(UsuarioDTO usuario) {

		try {
			// Log
			Util.logInfo("UsuarioImplementacion", "registrarUsuario", "Ha entrado");

			// Buscamos si existe un usuario con el email introducido
			UsuarioDTO usuarioEncontrado = obtieneUsuarioPorEmail(usuario.getEmailUsuario());

			if (usuarioEncontrado != null) {
				// Log
				Util.logInfo("UsuarioImplementacion", "registrarUsuario",
						"El usuario es null / No se ha encontrado usuario.");

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
				emailImplementacion.enviarEmail(url.getUrl() + "/activa-cuenta", true, usuarioDevuelto);
			}
			return usuarioDevuelto != null;
		} catch (IllegalArgumentException e) {
			// Log
			Util.logError("UsuarioImplementacion", "registrarUsuario",
					"Se ha intentado pasar a un método un argumento ilegal o inapropiado.");

			return null;
		} catch (OptimisticLockingFailureException e) {
			// Log
			Util.logError("UsuarioImplementacion", "registrarUsuario",
					"Se ha producido un error OptimisticLockingFailure.");

			// Excepcion de concurrencia optimista
			// Esto puede ocurrir si otro proceso ha modificado los datos mientras
			// esta transacción estaba realizando sus operaciones.
			return null;
		}
	}

	@Override
	public boolean activaCuenta(String token, String email) {
		try {
			// Log
			Util.logInfo("UsuarioImplementacion", "activaCuenta", "Ha entrado");

			// Obtenemos el token de la base de datos
			Token tokenDao = tokenImplementacion.obtieneToken(token);

			// Ahora comprobamos si el token no ha caducado
			// Obtenemos la fecha actual
			Calendar fechaActual = Calendar.getInstance();

			// Comparamos la fechaActual con la fecha del token
			if (fechaActual.compareTo(tokenDao.getFchFinToken()) < 0
					|| fechaActual.compareTo(tokenDao.getFchFinToken()) == 0) {
				// La fecha actual es menor que la fecha del token o son iguales, luego seguimos
				// con el proceso
				// Obtenemos el usuario del token
				Usuario usuarioDao = tokenDao.getUsuario();

				// Comprobamos si el email introducido es distinto al email del usuario
				if (!email.equals(usuarioDao.getEmailUsuario())) {
					// Log
					Util.logInfo("UsuarioImplementacion", "activaCuenta",
							"El email introducido no coincide con el email del usuario.");

					return false;
				}

				// Modificamos la propiedad estaActivado y la ponemos en true
				usuarioDao.setEstaActivadoUsuario(true);

				// Actualizamos en la base de datos
				Usuario usuarioDevuelto = usuarioRepositorio.save(usuarioDao);

				// Comprobamos si se ha actualizado correctamente
				if (usuarioDevuelto != null && usuarioDevuelto.isEstaActivadoUsuario()) {
					return true; // El usuario ha sido activado
				} else {
					// Log
					Util.logInfo("UsuarioImplementacion", "activaCuenta", "El usuario no ha sido activado.");
					return false; // El usuario no ha sido activado
				}
			} else {
				// Log
				Util.logInfo("UsuarioImplementacion", "activaCuenta", "El token ha caducado.");

				// La fecha actual es mayor que la fecha del token, luego ha caducado
				return false;
			}
		} catch (NullPointerException e) {
			// Log
			Util.logError("UsuarioImplementacion", "activaCuenta",
					"Se ha intentado pasar a un método un argumento null.");

			return false;
		} catch (IllegalArgumentException e) {
			// Log
			Util.logError("UsuarioImplementacion", "activaCuenta",
					"Se ha intentado pasar a un método un argumento ilegal o inapropiado.");

			return false;
		} catch (OptimisticLockingFailureException e) {
			// Log
			Util.logError("UsuarioImplementacion", "activaCuenta",
					"Se ha producido un error OptimisticLockingFailure.");

			return false;
		}
	}

	@Override
	public Boolean peticionCambiaPassword(String email) {
		try {
			// Log
			Util.logInfo("UsuarioImplementacion", "peticionCambiaPassword", "Ha entrado");

			// Obtenemos el usuario de la base de datos por el email
			Usuario usuarioEncontrado = Util.usuarioADao(obtieneUsuarioPorEmail(email));

			// Si usuarioEncontrado es null devolvemos false
			if (usuarioEncontrado == null) {
				// Log
				Util.logInfo("UsuarioImplementacion", "peticionCambiaPassword", "El email introducido no existe.");

				return false; // El email introducido no existe
			}

			// Si llega aqui es porque se ha encontrado el usuario.
			// Luego enviamos un email
			boolean ok = emailImplementacion.enviarEmail(url.getUrl() + "/restablecer/cambiar-password", false,
					usuarioEncontrado);

			// Controlamos la respuesta
			if (ok)
				return true; // Se ha enviado el correo correctamente
			else
				return null; // Se ha producido un error al enviar el correo
		} catch (Exception e) {
			// Log
			Util.logError("UsuarioImplementacion", "peticionCambiaPassword", "Se ha producido un error.");

			return null; // Se ha producido un error al enviar el correo
		}
	}

	@Override
	public boolean modificaPassword(String token, String password) {
		try {
			// Log
			Util.logInfo("UsuarioImplementacion", "modificaPassword", "Ha entrado");

			// Obtenemos el token
			Token tokenDao = tokenImplementacion.obtieneToken(token);
			
			// Comprobamos si el token se ha encontrado
			if(tokenDao == null)
				return false;
			
			// Ahora comprobamos si el token no ha caducado
			// Obtenemos la fecha actual
			Calendar fechaActual = Calendar.getInstance();

			// Comparamos la fechaActual con la fecha del token
			if (fechaActual.compareTo(tokenDao.getFchFinToken()) < 0
					|| fechaActual.compareTo(tokenDao.getFchFinToken()) == 0) {
				// La fecha actual es menor que la fecha del token o son iguales, luego seguimos
				// con el proceso
				// Obtenemos el usuario
				Usuario usuarioDao = tokenDao.getUsuario();

				// Modificamos la contraseña
				usuarioDao.setPsswdUsuario(password);

				// Actualizamos en la base de datos
				usuarioRepositorio.save(usuarioDao);

				return true;
			} else {
				// Log
				Util.logInfo("UsuarioImplementacion", "modificaPassword", "El token ha caducado.");

				// La fecha actual es mayor que la fecha del token, luego ha caducado
				return false;
			}
		} catch (NullPointerException e) {
			// Log
			Util.logError("UsuarioImplementacion", "modificaPassword",
					"Se ha intentado pasar a un método un argumento null.");

			return false;
		} catch (IllegalArgumentException e) {
			// Log
			Util.logError("UsuarioImplementacion", "modificaPassword",
					"Se ha intentado pasar a un método un argumento ilegal o inapropiado.");

			return false;
		} catch (OptimisticLockingFailureException e) {
			// Log
			Util.logError("UsuarioImplementacion", "modificaPassword",
					"Se ha producido un error OptimisticLockingFailure.");

			return false;
		}
	}

	@Override
	public boolean borraUsuarioPorId(long idUsuario) {
		try {
			// Log
			Util.logInfo("UsuarioImplementacion", "borraUsuarioPorId", "Ha entrado");

			// Comprobamos si existe un usuario con el id pasado
			UsuarioDTO usuarioEncontrado = obtieneUsuarioPorId(idUsuario);

			// Comprobamos si no se ha encontrado
			if (usuarioEncontrado == null) {
				// Log
				Util.logInfo("UsuarioImplementacion", "borraUsuarioPorId",
						"No se ha encontrado ningún usuario con el id pasado.");

				return false;
			}

			// Si existe comprobamos que no sea administrador
			if (usuarioEncontrado.getIdAcceso() == 2) {
				// Log
				Util.logInfo("UsuarioImplementacion", "borraUsuarioPorId",
						"Se ha intentando eliminar un usuario admin.");

				return false;
			}

			// Si no es admin lo eliminamos
			usuarioRepositorio.deleteById(idUsuario);

			// Ahora para comprobar si se ha eliminado vamos a buscar el usuario por el id
			UsuarioDTO usuarioDTO = obtieneUsuarioPorId(idUsuario);

			if (usuarioDTO == null)
				return true; // Devolvemos true si no existe

			return false; // En caso de que se haya encontrado un usuario con el id
		} catch (IllegalArgumentException e) {
			// Log
			Util.logError("UsuarioImplementacion", "borraUsuarioPorId",
					"Se ha intentado pasar a un método un argumento ilegal o inapropiado.");

			return false;
		}
	}

	@Override
	public boolean actualizaUsuario(UsuarioDTO usuarioDTO) {
		try {
			// Log
			Util.logInfo("UsuarioImplementacion", "actualizaUsuario", "Ha entrado");

			// Con el id del usuario pasado obtenemos el usuario de la base de datos
			Usuario usuarioEncontrado = Util.usuarioADao(obtieneUsuarioPorId(usuarioDTO.getIdUsuario()));

			// Comprobamos si existe algun usuario con el email introducido

			// Actualizamos algunos datos del usuarioEncontrado con el usuarioDTO
			Usuario usuarioDao = Util.usuarioADao(usuarioDTO);
			usuarioEncontrado.setNombreUsuario(usuarioDao.getNombreUsuario().trim());
			usuarioEncontrado.setEmailUsuario(usuarioDao.getEmailUsuario().trim());
			usuarioEncontrado.setTlfUsuario(usuarioDao.getTlfUsuario().trim());
			usuarioEncontrado.setEstaActivadoUsuario(usuarioDao.isEstaActivadoUsuario());
			usuarioEncontrado.setAcceso(usuarioDao.getAcceso());
			if (usuarioDTO.getImagenUsuario() != null)
				usuarioEncontrado.setImagenUsuario(Util.convertirAByteArray(usuarioDTO.getImagenUsuario()));

			// Actualizamos el usuario
			usuarioRepositorio.save(usuarioEncontrado);

			return true;
		} catch (IllegalArgumentException e) {
			// Log
			Util.logError("UsuarioImplementacion", "actualizaUsuario",
					"Se ha intentado pasar a un método un argumento ilegal o inapropiado.");

			return false;
		} catch (OptimisticLockingFailureException e) {
			// Log
			Util.logError("UsuarioImplementacion", "actualizaUsuario",
					"Se ha producido un error OptimisticLockingFailure.");

			return false;
		}
	}

	@Override
	public boolean agregaUsuario(UsuarioDTO usuarioDTO) {
		try {
			// Log
			Util.logInfo("UsuarioImplementacion", "agregaUsuario", "Ha entrado");

			// Buscamos si existe un usuario con el email introducido
			UsuarioDTO usuarioEncontrado = obtieneUsuarioPorEmail(usuarioDTO.getEmailUsuario());

			if (usuarioEncontrado != null) {
				// Log
				Util.logInfo("UsuarioImplementacion", "agregaUsuario",
						"No existe ningún usuario con el email introducido.");

				// Se ha encontrado un usuario con el email introducido
				// Luego devolveremos false
				return false;
			}

			// Si no se ha encontrado
			// Activamos la cuenta
			usuarioDTO.setEstaActivadoUsuario(true);

			// Guardamos el usuario
			usuarioRepositorio.save(Util.usuarioADao(usuarioDTO));

			return true;
		} catch (IllegalArgumentException e) {
			// Log
			Util.logError("UsuarioImplementacion", "agregaUsuario",
					"Se ha intentado pasar a un método un argumento ilegal o inapropiado.");

			return false;
		} catch (OptimisticLockingFailureException e) {
			// Log
			Util.logError("UsuarioImplementacion", "agregaUsuario",
					"Se ha producido un error OptimisticLockingFailure.");

			return false;
		}
	}

	@Override
	public boolean editarPerfil(UsuarioDTO usuarioActual, UsuarioDTO usuarioNuevo) {
		try {
			// Log
			Util.logInfo("UsuarioImplementacion", "editarPerfil", "Ha entrado");

			// Cambiamos los valores del usuarioActual
			usuarioActual.setNombreUsuario(usuarioNuevo.getNombreUsuario().trim());
			usuarioActual.setTlfUsuario(usuarioNuevo.getTlfUsuario().trim());
			if (usuarioNuevo.getImagenUsuario() != null)
				usuarioActual.setImagenUsuario(usuarioNuevo.getImagenUsuario());

			// Convertimos a DAO el usuarioActual
			Usuario usuarioDao = Util.usuarioADao(usuarioActual);

			// Actualizamos
			Usuario usuarioDevuelto = usuarioRepositorio.save(usuarioDao);

			if (usuarioDevuelto.getNombreUsuario().equals(usuarioNuevo.getNombreUsuario().trim())
					&& usuarioDevuelto.getTlfUsuario().equals(usuarioNuevo.getTlfUsuario().trim()))
				return true;
			else
				return false;
		} catch (Exception e) {
			// Log
			Util.logError("UsuarioImplementacion", "editarPerfil", "Se ha producido un error.");

			return false;
		}
	}

	@Override
	public boolean esUltimoAdmin(UsuarioDTO usuarioDTO) {
		try {
			// Log
			Util.logInfo("UsuarioImplementacion", "esUltimoAdmin", "Ha entrado");

			if (usuarioRepositorio.countAdminUsers() == 1) {
				// Si solo existe un admin tendremos que comprobar si el usuario es admin y si
				// le esta cambiando el rol
				// Primero obtenemos el usuario
				Usuario usuarioEncontrado = Util.usuarioADao(obtieneUsuarioPorId(usuarioDTO.getIdUsuario()));

				// Comprobamos el rol
				if (usuarioEncontrado.getAcceso().getCodAcceso().equals("Admin") && usuarioDTO.getIdAcceso() != 2) {
					// El usuario en la base de datos es admin y le esta cambiando a user
					return true; // Devolvemos true porque es el ultimo admin
				}
			}
			return false;
		} catch (Exception e) {
			// Log
			Util.logError("UsuarioImplementacion", "esUltimoAdmin", "Se ha producido un error.");

			return false;
		}
	}

	@Override
	public boolean existeUsuarioConEmail(UsuarioDTO usuarioDTO) {
		try {
			// Log
			Util.logInfo("UsuarioImplementacion", "existeUsuarioConEmail", "Ha entrado");

			// Primero obtenemos el usuario
			Usuario usuarioEncontrado = Util.usuarioADao(obtieneUsuarioPorId(usuarioDTO.getIdUsuario()));

			// Comprobamos si esta intentando cambiar el email
			if (!usuarioEncontrado.getEmailUsuario().equals(usuarioDTO.getEmailUsuario())) {
				// Se esta intentando cambiar de email
				// Ahora comprobamos si existe algun usuario con el email
				Usuario usuarioConEmail = usuarioRepositorio.findByEmailUsuario(usuarioDTO.getEmailUsuario());

				return usuarioConEmail == null ? false : true;
			}
			return false;
		} catch (Exception e) {
			// Log
			Util.logError("UsuarioImplementacion", "existeUsuarioConEmail", "Se ha producido un error.");

			return false;
		}
	}

	@Override
	public List<UsuarioDTO> obtieneUsuariosPorKeyword(String keyword) {
		try {
			// Log
			Util.logInfo("UsuarioImplementacion", "obtieneUsuariosPorKeyword", "Ha entrado");

			// Obtenemos todos los usuarios que contengan la keyword
			List<Usuario> listaUsuariosDao = usuarioRepositorio.findAllUsuariosByKeyword(keyword);

			// Comprobamos si se ha obtenido
			if (listaUsuariosDao != null)
				return Util.listaUsuarioDaoADto(listaUsuariosDao);
			// Si no se ha obtenido devolvemos null
			return null;
		} catch (Exception e) {
			// Log
			Util.logError("UsuarioImplementacion", "obtieneUsuariosPorKeyword", "Se ha producido un error.");

			return null;
		}
	}

	@Override
	public Boolean peticionActivaCuenta(String email) {
		try {
			// Log
			Util.logInfo("UsuarioImplementacion", "peticionActivaCuenta", "Ha entrado");

			// Obtenemos el usuario por el email
			Usuario usuarioEncontrado = usuarioRepositorio.findByEmailUsuario(email);

			// Comprobamos si existe
			if (usuarioEncontrado == null)
				return false; // No existe usuario con el email introducido

			// Si existe comprobamos si tiene la cuenta activada y si no la tiene mandamos
			// email
			if (!usuarioEncontrado.isEstaActivadoUsuario())
				return emailImplementacion.enviarEmail(url.getUrl() + "/activa-cuenta", true, usuarioEncontrado);

			return null; // Si el usuario ya tiene la cuenta activada
		} catch (Exception e) {
			// Log
			Util.logInfo("UsuarioImplementacion", "peticionActivaCuenta", "Se ha producido un error.");

			return false;
		}
	}

}
