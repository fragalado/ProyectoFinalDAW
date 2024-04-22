package com.example.demo.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador que controla las peticiones HTTP para la ruta /home
 * 
 * @author Francisco José Gallego Dorado
 * Fecha: 21/04/2024
 */
@Controller
@RequestMapping("/home")
public class HomeControlador {

	/**
	 * Método que maneja las solicitudes GET para la ruta /home
	 * @return Devuelve una vista
	 */
	@GetMapping
	public String vistaHome() {
		return "home";
	}
}
