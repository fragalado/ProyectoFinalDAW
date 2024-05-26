package com.example.demo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dtos.UsuarioDTO;
import com.example.demo.servicios.UsuarioImplementacion;
import com.example.demo.utiles.Util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador que controla las peticiones HTTP para la ruta /activa-cuenta
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
@Controller
@RequestMapping("/activa-cuenta")
public class ActivaCuentaControlador {

	@Autowired
	private UsuarioImplementacion usuarioImplementacion;

	/**
	 * Método que maneja las solicitudes GET para la ruta "/activa-cuenta/estado"
	 * 
	 * @param model   Objeto Model que proporciona Spring para enviar datos a la vista
	 * @param request Objeto HttpServletRequest que contiene información sobre la solicitud HTTP
	 * @return El nombre de la vista que se mostrará al usuario
	 */
	@GetMapping
	public String vistaActivarCuenta(@RequestParam("tk") String token, Model model, HttpServletRequest request) {

		try {
			Util.logInfo("ActivaCuentaControlador", "vistaActivarCuenta", "Ha entrado");
			// Control de sesion
			if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_USER")) {
				Util.logInfo("ActivaCuentaControlador", "vistaActivarCuenta", "El usuario ya ha iniciado sesion");
				return "redirect:/home";
			}
			
			// Añadimos el token al modelo
			model.addAttribute("token", token);
			
			// Creamos un nuevo objeto UsuarioDTO y lo agregamos al modelo
			model.addAttribute("usuarioDTO", new UsuarioDTO());

			// Devolvemos la vista register
			return "auth/confirmarEmail";
		} catch (Exception e) {
			Util.logError("ActivaCuentaControlador", "vistaActivarCuenta", "Se ha producido un error");
			return "redirect:/login";
		}
	}

	/**
	 * Método que maneja las solicitudes GET para la ruta "/activa-cuenta/activar"
	 * 
	 * @param token   Código del token
	 * @param request Objeto HttpServletRequest que contiene información sobre la solicitud HTTP
	 * @return El nombre de la vista que se mostrará al usuario
	 */
	@PostMapping
	public String activarCuenta(@ModelAttribute("token") String token, @RequestParam("email") String email, HttpServletRequest request) {

		try {
			Util.logInfo("ActivaCuentaControlador", "activarCuenta", "Ha entrado");
			// Control de sesion
			if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_USER")) {
				Util.logInfo("ActivaCuentaControlador", "activarCuenta", "El usuario ya ha iniciado sesion");
				return "redirect:/home";
			}

			// Controlamos que el token no este vacio
			if (token.isEmpty()) {
				Util.logInfo("ActivaCuentaControlador", "activarCuenta", "No hay token en la url");
				return "redirect:/login";
			}

			// Si el token no esta vacio vamos a activar la cuenta
			boolean ok = usuarioImplementacion.activaCuenta(token, email);

			if (ok) {
				// Redirigimos a activa-cuenta con un parametro success
				return "redirect:/activa-cuenta?success&tk="+token;
			} else {
				// Redirigimos a activa-cuenta con un parametro de error
				return "redirect:/activa-cuenta?error&tk="+token;
			}
		} catch (Exception e) {
			Util.logError("ActivaCuentaControlador", "activarCuenta", "Se ha producido un error");
			return "redirect:/login";
		}
	}
}
