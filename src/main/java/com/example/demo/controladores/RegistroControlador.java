package com.example.demo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dtos.UsuarioDTO;
import com.example.demo.servicios.UsuarioImplementacion;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Controlador que controla las peticiones HTTP para la ruta /registro
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
@Controller
@RequestMapping("/registro")
public class RegistroControlador {

	@Autowired
	private UsuarioImplementacion usuarioImplementacion;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * Método que maneja las solicitudes GET para la ruta "/register".
	 * 
	 * @param model   Objeto Model que proporciona Spring para enviar datos a la vista
	 * @param request Objeto HttpServletRequest que contiene información sobre la solicitud HTTP
	 * @return Devuelve el nombre de la vista
	 */
	@GetMapping
	public String vistaRegister(Model model, HttpServletRequest request) {

		try {
			// Control de sesion
			if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_USER")) {
				return "redirect:/home";
			}

			// Creamos un nuevo objeto UsuarioDTO y lo agregamos al modelo
			model.addAttribute("usuarioDTO", new UsuarioDTO());

			// Devolvemos la vista register
			return "register";
		} catch (Exception e) {
			return "register";
		}
	}

	/**
	 * Método que maneja las solicitudes POST para la ruta "/register"
	 * 
	 * @param usuario Objeto UsuarioDTO con los datos del formulario
	 * @param request Objeto HttpServletRequest que contiene información sobre la solicitud HTTP
	 * @return Devuelve una redirección
	 */
	@PostMapping
	public String registrarUsuario(@ModelAttribute("usuarioDTO") UsuarioDTO usuario, HttpServletRequest request) {
		try {
			// Encriptamos la contraseña
			usuario.setPsswd_usuario(bCryptPasswordEncoder.encode(usuario.getPsswd_usuario()));

			// Control de sesion
			if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_USER")) {
				return "redirect:/home";
			}

			// Registramos el usuario
			Boolean ok = usuarioImplementacion.registrarUsuario(usuario);

			// Controlamos la respuesta
			if (ok) {
				// Se ha registrado el usuario correctamente
				return "redirect:/registro?success";
			} else if (ok == null) {
				// Se ha producido un error al registrar el usuario
				return "redirect:/registro?error";
			} else {
				// El email ya existe
				return "redirect:/registro?email";
			}
		} catch (Exception e) {
			return "redirect:/registro?error";
		}
	}
}
