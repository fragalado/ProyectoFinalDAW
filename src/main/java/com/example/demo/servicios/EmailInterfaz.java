package com.example.demo.servicios;

import java.util.List;

import com.example.demo.daos.Usuario;
import com.example.demo.dtos.CarritoDTO;

/**
 * Interfaz que define los métodos que darán servicio al envío de emails.
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
public interface EmailInterfaz {

	/**
	 * Método que enviará un email para recuperar/modificar contraseña o activar
	 * cuenta al email pasado por parámetros.
	 * 
	 * @param direccion       Direccion a la que enviará el botón del html
	 * @param esActivarCuenta Boolean para saber si es correo para recuperar
	 *                        contraseña o para activar cuenta.
	 * @param usuario         Objeto Usuario (DAO)
	 */
	public boolean enviarEmail(String direccion, boolean esActivarCuenta, Usuario usuario);

	/**
	 * Método que envia un email de compra hecha
	 * 
	 * @param direccion       Direccion a la que enviará el botón del html
	 * @param emailUsuario    Email del usuario
	 * @param nombreUsuario   Nombre del usuario
	 * @param listaCarritoDTO Lista con objetos de tipo CarritoDTO
	 * @return Devuelve true si se ha enviado o false si no
	 */
	public boolean enviarEmailPedido(String direccion, String emailUsuario, String nombreUsuario,
			List<CarritoDTO> listaCarritoDTO);
}
