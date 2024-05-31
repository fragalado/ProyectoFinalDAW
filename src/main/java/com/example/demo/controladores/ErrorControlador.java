package com.example.demo.controladores;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.demo.utiles.Util;

/**
 * Controlador
 */
@ControllerAdvice
public class ErrorControlador {

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String error() {
		Util.logInfo("ErrorControlador", "error", "Ha entrado");
		return "error/error";
	}

	@GetMapping("/errorVista")
	public String vistaError() {
		Util.logInfo("ErrorControlador", "vistaError", "Ha entrado");
		return "error/error";
	}

}
