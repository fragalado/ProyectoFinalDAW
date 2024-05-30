package com.example.demo.controladores;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dtos.SuplementoDTO;
import com.example.demo.servicios.CarritoImplementacion;
import com.example.demo.servicios.SuplementoImplementacion;
import com.example.demo.utiles.Util;

/**
 * Clase controlador que controla las peticiones HTTP para la ruta /suplementos
 * 
 * Fecha: 15/05/2024
 * 
 * @author Francisco José Gallego Dorado
 */
@Controller
@RequestMapping("/suplementos")
public class SuplementoControlador {

	@Autowired
	private SuplementoImplementacion suplementoImplementacion;

	@Autowired
	private CarritoImplementacion carritoImplementacion;

	/**
	 * Método que controla las peticiones GET para la ruta /suplementos
	 * 
	 * @param modelo Objeto Model para pasar datos a la vista
	 * @return Devuelve una vista
	 */
	@GetMapping
	public String vistaSuplementos(Model modelo) {
		try {
			Util.logInfo("SuplementoControlador", "vistaSuplementos", "Ha entrado");
			// Obtenemos todos los suplementos
			List<SuplementoDTO> listaSuplementosDTO = suplementoImplementacion.obtieneTodosLosSuplementos();

			// Añadimos la lista al modelo
			modelo.addAttribute("listaSuplementosDTO", listaSuplementosDTO);

			// Obtenemos el numero de carrito del usuario
			modelo.addAttribute("tieneCarrito", carritoImplementacion.obtieneCantidadDeCarritosUsuario(
					SecurityContextHolder.getContext().getAuthentication().getName()));

			// Añadimos al modelo el tipo que esta visualizando, para poder volver a la vista
			modelo.addAttribute("tipo", 3);

			// Devolvemos la vista
			return "suplementos/suplementos";
		} catch (Exception e) {
			Util.logError("SuplementoControlador", "vistaSuplementos", "Se ha producido un error");
			return "redirect:/home";
		}
	}

	/**
	 * Método que controla las peticiones GET para la ruta /suplementos/{tipo}
	 * 
	 * @param tipo   Tipo del suplemento que se va a mostrar (1:Proteína;
	 *               2:Creatina; 3:Pre-Entrenamiento)
	 * @param modelo Objeto Model para pasar datos a la vista
	 * @return Devuelve una vista
	 */
	@GetMapping("/{tipo}")
	public String vistaSuplementosPorTipo(@PathVariable int tipo, Model modelo) {
		try {
			Util.logInfo("SuplementoControlador", "vistaSuplementosPorTipo", "Ha entrado");
			// Obtenemos todos los suplementos
			List<SuplementoDTO> listaSuplementosDTO = suplementoImplementacion.obtieneTodosLosSuplementos();

			// Actualizamos la lista si es necesario
			if (tipo == 1)
				listaSuplementosDTO = listaSuplementosDTO.stream().filter(x -> x.getTipoSuplemento().equals("Proteína"))
						.collect(Collectors.toList());
			else if (tipo == 2)
				listaSuplementosDTO = listaSuplementosDTO.stream().filter(x -> x.getTipoSuplemento().equals("Creatina"))
						.collect(Collectors.toList());
			else if (tipo == 3)
				listaSuplementosDTO = listaSuplementosDTO.stream()
						.filter(x -> x.getTipoSuplemento().equals("Pre-Entrenamiento")).collect(Collectors.toList());

			// Añadimos la lista al modelo
			modelo.addAttribute("listaSuplementosDTO", listaSuplementosDTO);

			// Obtenemos el numero de carrito del usuario
			modelo.addAttribute("tieneCarrito", carritoImplementacion.obtieneCantidadDeCarritosUsuario(
					SecurityContextHolder.getContext().getAuthentication().getName()));

			// Añadimos al modelo el tipo que esta visualizando, para poder volver a la
			// vista
			modelo.addAttribute("tipo", tipo);

			// Devolvemos la vista
			return "suplementos/suplementos";
		} catch (Exception e) {
			Util.logError("SuplementoControlador", "vistaSuplementosPorTipo", "Se ha producido un error");
			return "redirect:/home";
		}
	}

	/**
	 * Método que controla las peticiones GET para la ruta /suplementos/filter
	 * 
	 * @param keyword Keyword por la cual filtrar los suplementos
	 * @param modelo  Objeto Model para enviar datos a la vista
	 * @return Devuelve un fragmento de vista
	 */
	@GetMapping("/filter")
	public String filtrarSuplementoPorKeyword(String keyword, Model modelo) {
		try {
			// Log
			Util.logInfo("SuplementoControlador", "filtrarSuplementoPorKeyword", "Ha entrado");

			// Obtenemos los suplementos que contengan la keyword de nombre
			List<SuplementoDTO> listaSuplementoDTO = suplementoImplementacion.obtieneSuplementosPorKeyword(keyword);

			// Añadimos la lista al modelo
			modelo.addAttribute("listaSuplementosDTO", listaSuplementoDTO);

			// Devolvemos el fragmento actualizado
			return "suplementos/suplementos :: suplementosFragments";
		} catch (Exception e) {
			Util.logError("SuplementoControlador", "filtrarSuplementoPorKeyword", "Se ha producido un error");
			return "redirect:/home";
		}
	}
}
