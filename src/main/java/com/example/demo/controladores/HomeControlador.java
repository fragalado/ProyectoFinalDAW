package com.example.demo.controladores;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dtos.SuplementoDTO;
import com.example.demo.servicios.CarritoImplementacion;
import com.example.demo.servicios.SuplementoImplementacion;
import com.example.demo.utiles.Util;

/**
 * Controlador que controla las peticiones HTTP para la ruta /home
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
@Controller
@RequestMapping("/")
public class HomeControlador {

	@Autowired
	private SuplementoImplementacion suplementoImplementacion;

	@Autowired
	private CarritoImplementacion carritoImplementacion;

	/**
	 * Método que maneja las solicutes GET para la ruta /
	 * Redirecciona a la ruta /home
	 * @return Realiza un redirect
	 */
	@GetMapping
	public String redireccionaHome(){
		try {
			return "redirect:/home";
		} catch (Exception e) {
			return "redirect:/errorVista";
		}
	}

	/**
	 * Método que maneja las solicitudes GET para la ruta /home
	 * 
	 * @param modelo Objeto Model para pasar datos a la vista
	 * @return Devuelve una vista
	 */
	@GetMapping("/home")
	public String vistaHome(Model modelo, Authentication authentication) {
		try {
			Util.logInfo("HomeControlador", "vistaHome", "Ha entrado");
			
			// Obtenemos todos los suplementos
			List<SuplementoDTO> listaSuplementosDto = suplementoImplementacion.obtieneTodosLosSuplementos();

			// Añadimos al modelo los 6 primeros suplementos
			if (listaSuplementosDto.size() > 6)
				modelo.addAttribute("listaSuplementosDTO",
						listaSuplementosDto.stream().limit(6).collect(Collectors.toList()));
			else
				modelo.addAttribute("listaSuplementosDTO", listaSuplementosDto);

			// Obtenemos el numero de carrito del usuario
			if(authentication != null)
				modelo.addAttribute("tieneCarrito",
					carritoImplementacion.obtieneCantidadDeCarritosUsuario(authentication.getName()));

			// Devolvemos la vista
			return "home";
		} catch (Exception e) {
			Util.logError("HomeControlador", "vistaHome", "Ha entrado");
			return "redirect:/errorVista";
		}
	}
}
