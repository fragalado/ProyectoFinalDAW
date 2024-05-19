package com.example.demo.servicios;

import java.util.List;

import com.example.demo.dtos.CarritoDTO;

/**
 * Interfaz que define los métodos que darán servicio a carrito
 * 
 * Fecha: 15/05/2024
 * 
 * @author Francisco José Gallego Dorado
 */
public interface CarritoInterfaz {
	
	/**
	 * Método que obtiene el carrito de un usuario pasado por parámetros
	 * 
	 * @param emailUsuario Email del usuario
	 * @return Devuelve una lista de tipo CarritoDTO, List<CarritoDTO>
	 */
	public List<CarritoDTO> obtieneCarritoUsuario(String emailUsuario);

	/**
	 * Método que borra un carrito por su id
	 * 
	 * @param id_carrito Id del carrito a borrar
	 * @return Boolean; Devuelve true si se ha borrado o false si no
	 */
	public boolean borraCarrito(long id_carrito);

	/**
	 * Método que agrega un suplemento al carrito
	 * 
	 * @param id_suplemento Id del suplemento que se agrega
	 * @param email_usuario Email del usuario que lo agrega
	 * @return Boolean; Devuelve true si se ha agregado al carrito o false si no
	 */
	public boolean agregaSuplemento(long id_suplemento, String email_usuario);

	/**
	 * Método que obtiene el precio total del carrito
	 * 
	 * @param listaCarrito Lista de tipo CarritoDTO con los carritos del usuario
	 * @return Devuelve un tipo float; Precio total del carrito
	 */
	public float obtienePrecioTotalCarrito(List<CarritoDTO> listaCarrito);
}
