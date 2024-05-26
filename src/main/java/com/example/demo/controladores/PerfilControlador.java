package com.example.demo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.UsuarioDTO;
import com.example.demo.servicios.CarritoImplementacion;
import com.example.demo.servicios.UsuarioImplementacion;
import com.example.demo.utiles.Util;

/**
 * Controlador que controla las peticiones HTTP para la ruta /perfil
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 05/05/2024
 */
@Controller
@RequestMapping("/perfil")
public class PerfilControlador {

	@Autowired
	private UsuarioImplementacion usuarioImplementacion;

	@Autowired
	private CarritoImplementacion carritoImplementacion;

	/**
	 * Método que controla las peticiones GET para la ruta /perfil
	 * 
	 * @param modelo         Objeto Model para pasar datos a la vista
	 * @param authentication Objeto Authentication que contiene información sobre la
	 *                       autenticación
	 * @return Devuelve una vista
	 */
	@GetMapping()
	public String vistaPerfil(Model modelo, Authentication authentication) {
		try {
			Util.logInfo("PerfilControlador", "vistaPerfil", "Ha entrado");
			// Obtenemos el usuario por el email
			UsuarioDTO usuarioDto = usuarioImplementacion.obtieneUsuarioPorEmail(authentication.getName());

			// Comprobamos si no se ha encontrado
			if (usuarioDto == null)
				return "redirect:/errorVista";

			// Añadimos al modelo el usuario
			modelo.addAttribute("usuarioDTO", usuarioDto);

			// Obtenemos el numero de carrito del usuario
			modelo.addAttribute("tieneCarrito",
					carritoImplementacion.obtieneCantidadDeCarritosUsuario(authentication.getName()));

			// Devolvemos la vista
			return "perfil/perfil";
		} catch (Exception e) {
			Util.logError("PerfilControlador", "vistaPerfil", "Se ha producido un error");
			return "redirect:/errorVista";
		}
	}

	/**
	 * Método que controla las peticiones GET para la ruta /perfil/editar
	 * 
	 * @param modelo         Objeto Model para pasar datos a la vista
	 * @param authentication Objeto Authentication que contiene información sobre la
	 *                       autenticación
	 * @return Devuelve una vista
	 */
	@GetMapping("/editar")
	public String vistaEditarPerfil(Model modelo, Authentication authentication) {
		try {
			Util.logInfo("PerfilControlador", "vistaEditarPerfil", "Ha entrado");
			// Obtenemos el usuario por el email
			UsuarioDTO usuarioDto = usuarioImplementacion.obtieneUsuarioPorEmail(authentication.getName());

			// Comprobamos si no se ha encontrado
			if (usuarioDto == null)
				return "redirect:/errorVista";

			// Añadimos al modelo el usuario
			modelo.addAttribute("usuarioDTO", usuarioDto);

			// Obtenemos el numero de carrito del usuario
			modelo.addAttribute("tieneCarrito",
					carritoImplementacion.obtieneCantidadDeCarritosUsuario(authentication.getName()));

			// Devolvemos la vista
			return "perfil/editarPerfil";
		} catch (Exception e) {
			Util.logError("PerfilControlador", "vistaEditarPerfil", "Se ha producido un error");
			return "redirect:/errorVista";
		}
	}

	/**
	 * Método que controla las peticiones GET para la ruta /perfil/borrar
	 * 
	 * @param authentication Objeto Authentication que contiene información sobre la
	 *                       autenticación
	 * @return Devuelve una redirección
	 */
	@GetMapping("/borrar")
	public String borrarCuenta(Authentication authentication) {
		try {
			Util.logInfo("PerfilControlador", "borrarCuenta", "Ha entrado");
			// Obtenemos el usuario
			UsuarioDTO usuarioDto = usuarioImplementacion.obtieneUsuarioPorEmail(authentication.getName());

			// Eliminamos la cuenta
			usuarioImplementacion.borraUsuarioPorId(usuarioDto.getIdUsuario());

			return "redirect:/logout";
		} catch (Exception e) {
			Util.logError("PerfilControlador", "borrarCuenta", "Se ha producido un error");
			return "redirect:/errorVista";
		}
	}

	/**
	 * Método que controla las peticiones POST para la ruta /perfil/editar
	 * 
	 * @param usuarioDto Objeto UsuarioDTO con los nuevos datos del usuario
	 * @return Devuelve una redirección
	 */
	@PostMapping("/editar")
	public String editarPerfil(@ModelAttribute("usuarioDTO") UsuarioDTO usuarioDto,
			@RequestPart("imagenFile") MultipartFile file, Authentication authentication) {
		try {
			Util.logInfo("PerfilControlador", "editarPerfil", "Ha entrado");
			// Obtenemos el usuario actual
			UsuarioDTO usuarioActual = usuarioImplementacion.obtieneUsuarioPorEmail(authentication.getName());

			// Si la imagen no esta vacia se la añadimos al usuario
			if (!file.isEmpty()) {
				// Pasamos la imagen a base64
				String foto = Util.convertirABase64(file.getBytes());

				// Le añadimos la imagen al usuarioDTO
				usuarioDto.setImagenUsuario(foto);
			}

			// Actualizamos
			boolean ok = usuarioImplementacion.editarPerfil(usuarioActual, usuarioDto);

			// Controlamos la respuesta
			return ok ? "redirect:/perfil?editado" : "redirect:/perfil?error";
		} catch (Exception e) {
			Util.logError("PerfilControlador", "editarPerfil", "Se ha producido un error");
			return "redirect:/errorVista";
		}
	}
}
