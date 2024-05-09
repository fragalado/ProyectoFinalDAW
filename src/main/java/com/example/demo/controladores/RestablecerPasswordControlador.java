package com.example.demo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dtos.FormularioCambioPasswordDTO;
import com.example.demo.servicios.UsuarioImplementacion;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador que controla las peticiones HTTP para la ruta /restablecer
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
@Controller
@RequestMapping("/restablecer")
public class RestablecerPasswordControlador {

	@Autowired
	private UsuarioImplementacion usuarioImplementacion;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * Método que maneja las solicitudes GET para la ruta /restablecer.
	 * 
	 * @param request Objeto HttpServletRequest que contiene información sobre la
	 *                solicitud HTTP
	 * @return El nombre de la vista que se mostrará al usuario
	 */
	@GetMapping
	public String vistaPeticionPassword(HttpServletRequest request) {

		try {
			// Control de sesion
			if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_USER")) {
				return "redirect:/home";
			}

			// Devolvemos la vista register
			return "auth/peticionPassword";
		} catch (Exception e) {
			return "auth/peticionPassword";
		}
	}

	/**
	 * Método que maneja las solicitudes GET para la ruta
	 * /restablecer/cambia-password
	 * 
	 * @param token   Token
	 * @param model   Objeto Model que proporciona Spring para enviar datos a la
	 *                vista
	 * @param request Objeto HttpServletRequest que contiene información sobre la
	 *                solicitud HTTP
	 * @return El nombre de la vista que se mostrará al usuario
	 */
	@GetMapping("/cambiar-password")
	public String vistaCambiaPassword(@ModelAttribute("tk") String token, Model model, HttpServletRequest request) {

		try {
			// Control de sesion
			if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_USER")) {
				return "redirect:/home";
			}

			// Controlamos que el token no este vacio
			if (token.isEmpty()) {
				return "redirect:/login";
			}

			// Si el token no esta vacio:
			// Agregamos al model el token
			model.addAttribute("token", token);

			// Creamos un nuevo objeto FormularioCambioPasswordDTO y lo agregamos al modelo
			model.addAttribute("objetoDTO", new FormularioCambioPasswordDTO());

			// Devolvemos la vista
			return "auth/cambiarPassword";
		} catch (Exception e) {
			return "auth/cambiarPassword";
		}
	}

	/**
	 * Método que maneja las solicitudes POST para la ruta "/restablecer"
	 * 
	 * @param email   Email del usuario a cambiar la password
	 * @param request Objeto HttpServletRequest que contiene información sobre la
	 *                solicitud HTTP
	 * @return Devuelve el nombre de la vista
	 */
	@PostMapping
	public String peticionCambiarPassword(String email, HttpServletRequest request) {
		try {
			// Control de sesion
			if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_USER")) {
				return "redirect:/home";
			}

			// Realizamos la peticion para cambiar la contraseña.
			Boolean ok = usuarioImplementacion.peticionCambiaPassword(email);

			// Controlamos la respuesta
			if (ok) {
				// Se ha enviado el correo al usuario
				return "redirect:/restablecer?success";
			} else if (ok == null) {
				// Se ha producido un error
				return "redirect:/restablecer?error";
			} else {
				// El email introducido no existe
				return "redirect:/restablecer?email";
			}
		} catch (Exception e) {
			return "redirect:/restablecer?error";
		}
	}

	/**
	 * Método que maneja las solicitudes POST para la ruta
	 * /restablecer/cambiar-password
	 * 
	 * @param token   Token
	 * @param usuario Objeto UsuarioDTO que contiene los datos del formulario
	 * @param request Objeto HttpServletRequest que contiene información sobre la
	 *                solicitud HTTP
	 * @return Devuelve el nombre de la vista
	 */
	@PostMapping("/cambiar-password")
	public String cambiaPassword(@RequestParam String token,
			@ModelAttribute("objetoDTO") FormularioCambioPasswordDTO objetoDTO, HttpServletRequest request) {
		try {
			// Control de sesion
			if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_USER")) {
				return "redirect:/home";
			}

			// Encriptamos la contraseña
			objetoDTO.setPassword1(bCryptPasswordEncoder.encode(objetoDTO.getPassword1()));

			// Comprobamos si las contraseñas son iguales
			if(!bCryptPasswordEncoder.matches(objetoDTO.getPassword2(), objetoDTO.getPassword1())) {
				return "redirect:/restablecer/cambiar-password?password&tk=" + token;
			}

			// Modificamos la contraseña del usuario
			boolean ok = usuarioImplementacion.modificaPassword(token, objetoDTO.getPassword1());

			// Controlamos la respuesta
			if (ok)
				return "redirect:/restablecer/cambiar-password?success&tk=" + token;
			else
				return "redirect:/restablecer/cambiar-password?error&tk=" + token;
		} catch (Exception e) {
			return "redirect:/restablecer/cambiar-password?error&tk=" + token;
		}
	}
}
