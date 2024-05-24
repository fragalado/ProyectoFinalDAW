package com.example.demo.dtos;

import lombok.Data;

/**
 * Carrito DTO
 * 
 * Fecha: 15/05/2024
 * 
 * @author Francisco José Gallego Dorado
 */
@Data
public class CarritoDTO {

	// Atributos

	private long idCarrito;
	private long idUsuario;
	private SuplementoDTO suplementoDTO;
	private int cantidad;
	private boolean estaCompradoCarrito;

}
