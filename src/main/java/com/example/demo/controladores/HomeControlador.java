package com.example.demo.controladores;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dtos.SuplementoDTO;
import com.example.demo.servicios.SuplementoImplementacion;

/**
 * Controlador que controla las peticiones HTTP para la ruta /home
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
@Controller
@RequestMapping("/home")
public class HomeControlador {

	@Autowired
	private SuplementoImplementacion suplementoImplementacion;

	/**
	 * Método que maneja las solicitudes GET para la ruta /home
	 * 
	 * @param modelo Objeto Model para pasar datos a la vista
	 * @return Devuelve una vista
	 */
	@GetMapping
	public String vistaHome(Model modelo) {
		try {
			// Obtenemos todos los suplementos
			List<SuplementoDTO> listaSuplementosDto = suplementoImplementacion.obtieneTodosLosSuplementos();

			// Añadimos al modelo los 6 primeros suplementos
			if (listaSuplementosDto.size() > 6)
				modelo.addAttribute("listaSuplementosDTO",
						listaSuplementosDto.stream().limit(6).collect(Collectors.toList()));
			else
				modelo.addAttribute("listaSuplementosDTO", listaSuplementosDto);

			// Devolvemos la vista
			return "home";
		} catch (Exception e) {
			return "redirect:/errorVista";
		}
	}
}
