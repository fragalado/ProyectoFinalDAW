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

	private long id_carrito;
	private long id_usuario;
	private SuplementoDTO suplementoDTO;
	private int cantidad;
	private boolean estaComprado_carrito;

}
