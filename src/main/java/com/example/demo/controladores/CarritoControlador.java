package com.example.demo.controladores;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dtos.CarritoDTO;
import com.example.demo.servicios.CarritoImplementacion;
import com.example.demo.utiles.Util;

/**
 * Controlador que controla las peticiones HTTP para la ruta /carrito
 * 
 * Fecha: 15/05/2024
 * 
 * @author Francisco José Gallego Dorado
 */
@Controller
@RequestMapping("/carrito")
public class CarritoControlador {

	@Autowired
	private CarritoImplementacion carritoImplementacion;

	/**
	 * Método que controla las peticiones GET para la ruta /carrito
	 * 
	 * @param modelo         Objeto Model para pasar datos a la vista
	 * @param authentication Objeto Authentication con los datos de la sesión
	 * @return Devuelve una vista
	 */
	@GetMapping
	public String vistaCarrito(Model modelo, Authentication authentication) {
		try {
			Util.logInfo("CarritoControlador", "vistaCarrito", "Ha entrado");
			// Obtenemos el carrito del usuario
			List<CarritoDTO> listaCarritoDTO = carritoImplementacion.obtieneCarritoUsuario(authentication.getName());

			// Añadimos al modelo una lista de carrito DTO
			if (listaCarritoDTO == null)
				modelo.addAttribute("listaCarritoDTO", new ArrayList<CarritoDTO>());
			else {
				modelo.addAttribute("listaCarritoDTO", listaCarritoDTO);
				modelo.addAttribute("precioTotal", carritoImplementacion.obtienePrecioTotalCarrito(listaCarritoDTO));
			}

			// Devolvemos la vista
			return "carrito/carrito";
		} catch (Exception e) {
			Util.logError("CarritoControlador", "vistaCarrito", "Se ha producido un error");
			return "redirect:/home";
		}
	}

	/**
	 * Método que controla las peticiones GET para la ruta /carrito/agrega-suplemento/{idSuplemento}
	 * 
	 * @param idSuplemento   Id del suplemento a agregar al carrito.
	 * @param authentication Objeto Authentication con los datos de la sesión.
	 * @return Devuelve una redirección
	 */
	@GetMapping("/agrega-suplemento/{idSuplemento}")
	public String agregaSuplementoAlCarrito(@PathVariable long idSuplemento, Authentication authentication, @RequestParam String tipo) {
		try {
			Util.logInfo("CarritoControlador", "agregaSuplementoAlCarrito", "Ha entrado");
			// Agregamos el suplemento al carrito
			boolean ok = carritoImplementacion.agregaSuplemento(idSuplemento, authentication.getName());

			// Controlamos la respuesta
			String url = "redirect:/suplementos/"+tipo;
			return ok ? url + "?success" : url + "?error";
		} catch (Exception e) {
			Util.logError("CarritoControlador", "agregaSuplementoAlCarrito", "Se ha producido un error");
			return "redirect:/home";
		}
	}

	/**
	 * Método que controla las peticiones GET para la ruta /carrito/borra-suplemento/{idCarrito}
	 * 
	 * @param idCarrito Id del carrito a eliminar
	 * @return Devuelve una redirección
	 */
	@GetMapping("/borra-suplemento/{idCarrito}")
	public String borraSuplementoCarrito(@PathVariable long idCarrito) {
		try {
			Util.logInfo("CarritoControlador", "borraSuplementoCarrito", "Ha entrado");
			// Borramos el carrito
			boolean ok = carritoImplementacion.borraCarrito(idCarrito);

			// Controlamos la respuesta
			return ok ? "redirect:/carrito?success" : "redirect:/carrrito?error";
		} catch (Exception e) {
			Util.logError("CarritoControlador", "borraSuplementoCarrito", "Se ha producido un error");
			return "redirect:/home";
		}
	}
}
