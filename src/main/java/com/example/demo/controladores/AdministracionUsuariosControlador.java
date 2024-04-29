package com.example.demo.controladores;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.UsuarioDTO;
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
			// Obtenemos una lista con todos los usuarios y la ordenamos por el id_acceso
			List<UsuarioDTO> listaUsuarios = usuarioImplementacion.obtieneTodosLosUsuarios().stream()
					.sorted(Comparator.comparingLong(UsuarioDTO::getId_acceso).reversed()).collect(Collectors.toList());
			
			// Agregamos la lista al modelo
			model.addAttribute("listaUsuariosDTO", listaUsuarios);

			// Devolvemos la vista
			return "admin/usuarios/administracionUsuarios";
		} catch (Exception e) {
			return "redirect:/home";
		}
	}

	/**
	 * Método que maneja las solicitudes GET para la ruta "/admin/usuarios/editar/{id_usuario}"
	 * 
	 * @param id_usuario Id del usuario a editar
	 * @param model      Objeto Model que proporciona Spring para enviar datos a la vista
	 * @return Devuelve el nombre de la vista
	 */
	@GetMapping("/editar/{id_usuario}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String vistaEditarUsuario(@PathVariable long id_usuario, Model model) {
		try {
			// Obtenemos el usuario de la base de datos y lo agregamos al modelo
			UsuarioDTO usuarioDTO = usuarioImplementacion.obtieneUsuarioPorId(id_usuario);

			// Comprobamos si se ha encontrado un usuario con el id pasado
			if(usuarioDTO == null)
				return "redirect:/errorVista";
						
			// Lo agregamos al modelo
			model.addAttribute("usuarioDTO", usuarioDTO);

			// Devolvemos la vista
			return "admin/usuarios/editarUsuario";
		} catch (Exception e) {
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
			// Agregamos al modelo un objeto de tipo UsuarioDTO
			model.addAttribute("usuarioDTO", new UsuarioDTO());

			// Devolvemos el nombre de la vista
			return "admin/usuarios/agregarUsuario";
		} catch (Exception e) {
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
			// Eliminamos el usuario por el id_usuario
			boolean ok = usuarioImplementacion.borraUsuarioPorId(id_usuario);

			// Devolvemos la vista con un parametro segun se haya elimando o no
			if (ok)
				return "redirect:/admin/usuarios?success";
			else
				return "redirect:/admin/usuarios?error";
		} catch (Exception e) {
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
	public String editarUsuario(@ModelAttribute("usuarioDTO") UsuarioDTO usuario, @RequestPart("imagenFile") MultipartFile imagenFile) {
		try {
			// Controlamos los valores
			if (usuario.getNombre_usuario().length() > 50 || usuario.getEmail_usuario().length() > 50
					|| usuario.getTlf_usuario().length() > 15)
				return "redirect:/admin/administracion-usuarios?usuarioAgregadoError";

			// Si la imagen no esta vacia se la añadimos al usuario
			if (!imagenFile.isEmpty()) {
				// Pasamos la imagen a String
				String foto = Util.convertirABase64(imagenFile.getBytes());

				// Le añadimos la imagen al usuarioDTO
				usuario.setImagen_usuario(foto);
			}

			// Actualizamos el usuario
			boolean ok = usuarioImplementacion.actualizaUsuario(usuario);

			if (ok)
				return "redirect:/admin/usuarios?usuarioEditadoSuccess";
			else
				return "redirect:/admin/usuarios?usuarioEditadoError";
		} catch (Exception e) {
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
	public String agregarUsuario(@ModelAttribute("usuarioDTO") UsuarioDTO usuarioDTO, @RequestPart("imagenFile") MultipartFile imagenFile) {
		try {
			// Encriptamos la contraseña
			usuarioDTO.setPsswd_usuario(bCryptPasswordEncoder.encode(usuarioDTO.getPsswd_usuario()));

			// Controlamos los valores
			if (usuarioDTO.getNombre_usuario().length() > 50 || usuarioDTO.getEmail_usuario().length() > 50
					|| usuarioDTO.getTlf_usuario().length() > 15 || usuarioDTO.getPsswd_usuario().length() > 255)
				return "redirect:/admin/usuarios?usuarioAgregadoError";

			// Si la imagen no esta vacia se la añadimos al usuario
			if (!imagenFile.isEmpty()) {
				// Pasamos la imagen a String
				String foto = Util.convertirABase64(imagenFile.getBytes());

				// Le añadimos la imagen al usuarioDTO
				usuarioDTO.setImagen_usuario(foto);
			}

			// Agregamos el usuario
			boolean ok = usuarioImplementacion.agregaUsuario(usuarioDTO);

			if (ok)
				return "redirect:/admin/usuarios?usuarioAgregadoSuccess";
			else
				return "redirect:/admin/usuarios?usuarioAgregadoExiste";
		} catch (Exception e) {
			return "redirect:/admin/usuarios?usuarioAgregadoError";
		}
	}
}
