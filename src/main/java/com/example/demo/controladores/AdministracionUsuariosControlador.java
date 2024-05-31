package com.example.demo.controladores;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.UsuarioDTO;
import com.example.demo.servicios.CarritoImplementacion;
import com.example.demo.servicios.UsuarioImplementacion;
import com.example.demo.utiles.Util;

/**
 * Controlador que controla las peticiones HTTP para la ruta /admin
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 27/04/2024
 */
@Controller
@RequestMapping("/admin/usuarios")
public class AdministracionUsuariosControlador {

	@Autowired
	private UsuarioImplementacion usuarioImplementacion;

	@Autowired
	private CarritoImplementacion carritoImplementacion;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * Método que maneja las solicitudes GET para la ruta "/admin/usuarios"
	 * 
	 * @param model Objeto Model que proporciona Spring para enviar datos a la vista
	 * @return Devuelve el nombre de la vista
	 */
	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String vistaAdministracionUsuarios(Model model) {
		try {
			Util.logInfo("AdministracionUsuariosControlador", "vistaAdministracionUsuarios", "Ha entrado");
			
			// Obtenemos una lista con todos los usuarios y la ordenamos por el id_acceso
			List<UsuarioDTO> listaUsuarios = usuarioImplementacion.obtieneTodosLosUsuarios().stream()
					.sorted(Comparator.comparingLong(UsuarioDTO::getIdAcceso).reversed()).collect(Collectors.toList());

			// Agregamos la lista al modelo
			model.addAttribute("listaUsuariosDTO", listaUsuarios);

			// Obtenemos el numero de carrito del usuario
			model.addAttribute("tieneCarrito", carritoImplementacion.obtieneCantidadDeCarritosUsuario(
					SecurityContextHolder.getContext().getAuthentication().getName()));

			// Devolvemos la vista
			return "admin/usuarios/administracionUsuarios";
		} catch (Exception e) {
			Util.logError("AdministracionUsuariosControlador", "vistaAdministracionUsuarios",
					"Se ha producido un error");
			return "redirect:/home";
		}
	}

	/**
	 * Método que controla las peticiones GET para la ruta /admin/usuarios/filter
	 * @param modelo Objeto Model para pasar datos a la vista
	 * @param keyword keyword
	 * @return Devuelve un fragmento de vista
	 */
	@GetMapping("/filter")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String filtrarUsuarios(Model modelo, @RequestParam String keyword) {
		try {
			// Log
			Util.logInfo("AdministracionUsuariosControlador", "filtrarUsuarios", "Ha entrado");

			// Obtenemos la lista de usuarios que el email contiene la keyword
			List<UsuarioDTO> listaUsuarios = usuarioImplementacion.obtieneUsuariosPorKeyword(keyword);

			// Añadimos la lista al modelo
			modelo.addAttribute("listaUsuariosDTO", listaUsuarios);

			// Devolvemos la vista
			return "admin/usuarios/administracionUsuarios :: userTable"; // Devolvemos el fragment para no que devuelva la vista entera
		} catch (Exception e) {
			Util.logError("AdministracionUsuariosControlador", "filtrarUsuarios", "Se ha producido un error");
			return "redirect:/home";
		}
	}

	/**
	 * Método que maneja las solicitudes GET para la ruta "/admin/usuarios/editar/{id_usuario}"
	 * 
	 * @param id_usuario Id del usuario a editar
	 * @param model      Objeto Model que proporciona Spring para enviar datos a la
	 *                   vista
	 * @return Devuelve el nombre de la vista
	 */
	@GetMapping("/editar/{id_usuario}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String vistaEditarUsuario(@PathVariable long id_usuario, Model model) {
		try {
			Util.logInfo("AdministracionUsuariosControlador", "vistaEditarUsuario", "Ha entrado");
			
			// Obtenemos el usuario de la base de datos y lo agregamos al modelo
			UsuarioDTO usuarioDTO = usuarioImplementacion.obtieneUsuarioPorId(id_usuario);

			// Comprobamos si se ha encontrado un usuario con el id pasado
			if (usuarioDTO == null)
				return "redirect:/errorVista";

			// Lo agregamos al modelo
			model.addAttribute("usuarioDTO", usuarioDTO);

			// Obtenemos el numero de carrito del usuario
			model.addAttribute("tieneCarrito", carritoImplementacion.obtieneCantidadDeCarritosUsuario(
					SecurityContextHolder.getContext().getAuthentication().getName()));

			// Devolvemos la vista
			return "admin/usuarios/editarUsuario";
		} catch (Exception e) {
			Util.logError("AdministracionUsuariosControlador", "vistaEditarUsuario", "Se ha producido un error");
			return "redirect:/home";
		}
	}

	/**
	 * Método que maneja las solicitudes GET para la ruta "/admin/usuarios/agregar"
	 * 
	 * @param model Objeto Model que proporciona Spring para enviar datos a la vista
	 * @return Devuelve el nombre de la vista
	 */
	@GetMapping("/agregar")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String vistaAgregarUsuario(Model model) {
		try {
			Util.logInfo("AdministracionUsuariosControlador", "vistaAgregarUsuario", "Ha entrado");
			
			// Agregamos al modelo un objeto de tipo UsuarioDTO
			model.addAttribute("usuarioDTO", new UsuarioDTO());

			// Obtenemos el numero de carrito del usuario
			model.addAttribute("tieneCarrito", carritoImplementacion.obtieneCantidadDeCarritosUsuario(
					SecurityContextHolder.getContext().getAuthentication().getName()));

			// Devolvemos el nombre de la vista
			return "admin/usuarios/agregarUsuario";
		} catch (Exception e) {
			Util.logError("AdministracionUsuariosControlador", "vistaAgregarUsuario", "Se ha producido un error");
			return "redirect:/home";
		}
	}

	/**
	 * Método que maneja las solicitudes GET para la ruta "/admin/usuarios/borrar/{id_usuario}"
	 * 
	 * @param id_usuario Id del usuario a borrar
	 * @return Devuelve el nombre de la vista
	 */
	@GetMapping("/borrar/{id_usuario}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String borraUsuario(@PathVariable long id_usuario) {
		try {
			Util.logInfo("AdministracionUsuariosControlador", "borraUsuario", "Ha entrado");
			
			// Eliminamos el usuario por el id_usuario
			boolean ok = usuarioImplementacion.borraUsuarioPorId(id_usuario);

			// Devolvemos la vista con un parametro segun se haya elimando o no
			if (ok)
				return "redirect:/admin/usuarios?success";
			else
				return "redirect:/admin/usuarios?error";
		} catch (Exception e) {
			Util.logError("AdministracionUsuariosControlador", "borraUsuario", "Se ha producido un error");
			return "redirect:/admin/usuarios?error";
		}
	}

	/**
	 * Método que maneja las solicitudes POST para la ruta "/admin/usuarios/editar"
	 * 
	 * @param usuario    Objeto usuario con los datos del formulario
	 * @param imagenFile Objeto MultipartFile que contiene la imagen
	 * @return Devuelve el nombre de la vista
	 */
	@PostMapping("/editar")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String editarUsuario(@ModelAttribute("usuarioDTO") UsuarioDTO usuario,
			@RequestPart("imagenFile") MultipartFile imagenFile) {
		try {
			Util.logInfo("AdministracionUsuariosControlador", "editarUsuario", "Ha entrado");
			
			// Controlamos los valores
			if (usuario.getNombreUsuario().length() > 50 || usuario.getEmailUsuario().length() > 50
					|| usuario.getTlfUsuario().length() > 15)
				return "redirect:/admin/administracion-usuarios?usuarioAgregadoError";

			// Si la imagen no esta vacia se la añadimos al usuario
			if (!imagenFile.isEmpty()) {
				// Pasamos la imagen a String
				String foto = Util.convertirABase64(imagenFile.getBytes());

				// Le añadimos la imagen al usuarioDTO
				usuario.setImagenUsuario(foto);
			}

			// Comprobamos si es el ultimo admin y le estamos cambiando el rol
			if (usuarioImplementacion.esUltimoAdmin(usuario)) {
				return "redirect:/admin/usuarios?esUltimoAdmin";
			}

			// Comprobamos si esta cambiando el email y si ya existe
			if (usuarioImplementacion.existeUsuarioConEmail(usuario)) {
				return "redirect:/admin/usuarios?emailExiste";
			}

			// Actualizamos el usuario
			boolean ok = usuarioImplementacion.actualizaUsuario(usuario);

			if (ok)
				return "redirect:/admin/usuarios?usuarioEditadoSuccess";
			else
				return "redirect:/admin/usuarios?usuarioEditadoError";
		} catch (Exception e) {
			Util.logError("AdministracionUsuariosControlador", "editarUsuario", "Se ha producido un error");
			return "redirect:/admin/usuarios?usuarioEditadoError";
		}
	}

	/**
	 * Método que realiza la operación de agregar un usuario a la base de datos.
	 * Controla las peticiones POST para la ruta /admin/usuarios/agregar
	 * 
	 * @param usuarioDTO Objeto UsuarioDTO con los datos del usuario a agregar.
	 * @param imagenFile Objeto MultipartFile con la imagen del usuario.
	 * @return Devuelve una redirección.
	 */
	@PostMapping("/agregar")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String agregarUsuario(@ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO,
			@RequestPart("imagenFile") MultipartFile imagenFile) {
		try {
			Util.logInfo("AdministracionUsuariosControlador", "agregarUsuario", "Ha entrado");
			
			// Encriptamos la contraseña
			usuarioDTO.setPsswdUsuario(bCryptPasswordEncoder.encode(usuarioDTO.getPsswdUsuario().trim()));

			// Controlamos los valores
			if (usuarioDTO.getNombreUsuario().length() > 50 || usuarioDTO.getEmailUsuario().length() > 50
					|| usuarioDTO.getTlfUsuario().length() > 15 || usuarioDTO.getPsswdUsuario().length() > 255)
				return "redirect:/admin/usuarios?usuarioAgregadoError";

			// Si la imagen no esta vacia se la añadimos al usuario
			if (!imagenFile.isEmpty()) {
				// Pasamos la imagen a String
				String foto = Util.convertirABase64(imagenFile.getBytes());

				// Le añadimos la imagen al usuarioDTO
				usuarioDTO.setImagenUsuario(foto);
			}

			// Controlamos los espacios en blanco de las propiedades
			usuarioDTO.setNombreUsuario(usuarioDTO.getNombreUsuario().trim());
			usuarioDTO.setEmailUsuario(usuarioDTO.getEmailUsuario().trim());
			usuarioDTO.setTlfUsuario(usuarioDTO.getTlfUsuario().trim());

			// Agregamos el usuario
			boolean ok = usuarioImplementacion.agregaUsuario(usuarioDTO);

			if (ok)
				return "redirect:/admin/usuarios?usuarioAgregadoSuccess";
			else
				return "redirect:/admin/usuarios?usuarioAgregadoExiste";
		} catch (Exception e) {
			Util.logError("AdministracionUsuariosControlador", "agregarUsuario", "Se ha producido un error");
			return "redirect:/admin/usuarios?usuarioAgregadoError";
		}
	}
}
