package com.example.demo.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dtos.UsuarioDTO;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador que controla las peticiones HTTP para la ruta /login
 * 
 * @author Francisco José Gallego Dorado Fecha: 21/04/2024
 */
@Controller
@RequestMapping("/login")
public class LoginControlador {

	/**
	 * Método que maneja las solicitudes GET para la ruta "/login"
	 * 
	 * @param model   Objeto Model que proporciona Spring para enviar datos a la vista
	 * @param request Objeto HttpServletRequest que contiene información sobre la solicitud HTTP
	 * @return Devuelve el nombre de la vista
	 */
	@GetMapping
	public String vistaLogin(Model model, HttpServletRequest request) {

		try {
			// Control de sesion
			if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_USER")) {
				return "redirect:/home";
			}

			// Creamos un nuevo objeto UsuarioDTO y lo agregamos al modelo
			model.addAttribute("usuarioDTO", new UsuarioDTO());

			// Devolvemos la vista login
			return "auth/login";
		} catch (Exception e) {
			return "auth/login";
		}
	}
}
