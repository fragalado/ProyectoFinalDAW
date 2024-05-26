package com.example.demo.servicios;

import java.util.List;

import com.example.demo.dtos.OrdenDTO;

/**
 * Interfaz que define los métodos que darán servicio a Orden
 * 
 * Fecha: 19/05/2024
 * @author Francisco José Gallego Dorado
 */
public interface OrdenInterfaz {

	/**
	 * Método que realiza la compra de los carritos del usuario pasado por parámetros
	 * @param emailUsuario Email del usuario
	 * @return Devuelve true si se ha realizado la compra o false si no.
	 */
	public boolean compraCarritoUsuario(String emailUsuario);
	
	/**
	 * Método que obtiene todas las compras de un usuario y las devuelve en una lista de objetos OrdenDTO
	 * @param emailUsuario Email del usuario
	 * @return List de objetos OrdenDTO
	 */
	public List<OrdenDTO> obtieneComprasUsuario(String emailUsuario);
}
