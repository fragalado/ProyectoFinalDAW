package com.example.demo.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dtos.OrdenDTO;
import com.example.demo.servicios.OrdenImplementacion;
import com.example.demo.utiles.Util;

/**
 * Controlador que controla las peticiones HTTP para la ruta /pedidos
 * 
 * Fecha: 24/05/2024
 * 
 * @author Francisco Jose Gallego Dorado
 */
@Controller
@RequestMapping("/pedidos")
public class PedidosControlador {

	@Autowired
	private OrdenImplementacion ordenImplementacion;

	@GetMapping
	public String vistaPedidos(Model modelo, Authentication authentication) {

		try {
			Util.logInfo("PedidosControlador", "vistaPedidos", "Ha entrado");
			// Obtenemos todos los pedidos del usuario
			List<OrdenDTO> listaOrdenDto = ordenImplementacion.obtieneComprasUsuario(authentication.getName());

			// Añadimos la lista al modelo
			modelo.addAttribute("listaOrdenDto", listaOrdenDto);

			// Devolvemos la vista
			return "pedidos/pedidos";
		} catch (Exception e) {
			Util.logError("PedidosControlador", "vistaPedidos", "Se ha producido un error");
			return "redirect:/errorVista";
		}
	}
}
