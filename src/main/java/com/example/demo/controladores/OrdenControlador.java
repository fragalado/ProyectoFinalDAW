package com.example.demo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.paypal.PaypalService;
import com.example.demo.servicios.OrdenImplementacion;
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

	@PostMapping
	public String comprarCarrito(@RequestParam Double total) {
		String cancelUrl = "http://localhost:8080/comprar/cancel";
		String successUrl = "http://localhost:8080/comprar/success";
		try {
			try {
				Payment payment = paypalService.createPayment(total, "EUR", "payment description", cancelUrl, successUrl);
				for (Links links : payment.getLinks()) {
					if (links.getRel().equals("approval_url")) {
						return "redirect:" + links.getHref();
					}
				}
			} catch (PayPalRESTException e) {
				System.out.println("Error paypal");
			}
			return "redirect:/carrito";
		} catch (Exception e) {
			return "redirect:/home?error";
		}
	}

	@GetMapping("/cancel")
	public String cancelPay() {
		return "redirect:/carrito?payCancel";
	}

	@GetMapping("/success")
	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, Authentication authentication) {
		try {
			Payment payment = paypalService.executePayment(paymentId, payerId);
			if(payment.getState().equals("approved")){
				// Realizamos la compra del carrito
				boolean ok = ordenImplementacion.compraCarritoUsuario(authentication.getName());
				return ok ? "redirect:/carrito?paySuccess" : "redirect:/carrito?error";
			}
			return "redirect:/carrito?payError";
		} catch (PayPalRESTException e) {
			return "redirect:/carrito?payError";
		}
	}
}
