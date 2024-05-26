package com.example.demo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.config.UrlProperties;
import com.example.demo.paypal.PaypalService;
import com.example.demo.servicios.OrdenImplementacion;
import com.example.demo.utiles.Util;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

/**
 * Controlador que controla las peticiones HTTP para la ruta /comprar
 * 
 * Fecha: 19/05/2024
 * 
 * @author Francisco José Gallego Dorado
 */
@Controller
@RequestMapping("/comprar")
public class OrdenControlador {

	@Autowired
	private PaypalService paypalService;

	@Autowired
	private OrdenImplementacion ordenImplementacion;

	@Autowired
	private UrlProperties url;

	@PostMapping
	public String comprarCarrito(@RequestParam Double total) {
		String cancelUrl = url.getUrl() + "/comprar/cancel";
		String successUrl = url.getUrl() + "/comprar/success";
		try {
			Util.logInfo("OrdenControlador", "comprarCarrito", "Ha entrado");
			Payment payment = paypalService.createPayment(total, "EUR", "payment description", cancelUrl, successUrl);
			for (Links links : payment.getLinks()) {
				if (links.getRel().equals("approval_url")) {
					return "redirect:" + links.getHref();
				}
			}
			return "redirect:/carrito";
		} catch (Exception e) {
			Util.logError("OrdenControlador", "comprarCarrito", "Se ha producido un error");
			return "redirect:/home?error";
		}
	}

	@GetMapping("/cancel")
	public String cancelPay() {
		try {
			Util.logInfo("OrdenControlador", "cancelPay", "Ha entrado");
			return "redirect:/carrito?payCancel";
		} catch (Exception e) {
			Util.logError("OrdenControlador", "cancelPay", "Se ha producido un error");
			return "redirect:/home?error";
		}

	}

	@GetMapping("/success")
	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,
			Authentication authentication) {
		try {
			Util.logInfo("OrdenControlador", "successPay", "Ha entrado");
			Payment payment = paypalService.executePayment(paymentId, payerId);
			if (payment.getState().equals("approved")) {
				// Realizamos la compra del carrito
				boolean ok = ordenImplementacion.compraCarritoUsuario(authentication.getName());
				return ok ? "redirect:/carrito?paySuccess" : "redirect:/carrito?error";
			}
			return "redirect:/carrito?payError";
		} catch (PayPalRESTException e) {
			Util.logError("OrdenControlador", "successPay", "Se ha producido un error");
			return "redirect:/carrito?payError";
		}
	}
}
