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
	 * @param idCarrito Id del carrito a borrar
	 * @param emailUsuario Email del usuario que está eliminando el carrito
	 * @return Boolean; Devuelve true si se ha borrado o false si no
	 */
	public boolean borraCarrito(long idCarrito, String emailUsuario);

	/**
	 * Método que agrega un suplemento al carrito
	 * 
	 * @param idSuplemento Id del suplemento que se agrega
	 * @param emailUsuario Email del usuario que lo agrega
	 * @return Boolean; Devuelve true si se ha agregado al carrito o false si no
	 */
	public boolean agregaSuplemento(long idSuplemento, String emailUsuario);

	/**
	 * Método que obtiene el precio total del carrito
	 * 
	 * @param listaCarrito Lista de tipo CarritoDTO con los carritos del usuario
	 * @return Devuelve un tipo float; Precio total del carrito
	 */
	public float obtienePrecioTotalCarrito(List<CarritoDTO> listaCarrito);

	/**
	 * Método que suma todos los carritos de un usuario
	 * 
	 * @param emailUsuario Email del usuario
	 * @return Devuelve la cantidad de carritos de un usuario
	 */
	public int obtieneCantidadDeCarritosUsuario(String emailUsuario);
}
