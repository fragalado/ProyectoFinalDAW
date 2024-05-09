package com.example.demo.servicios;

import java.util.List;

import com.example.demo.dtos.SuplementoDTO;

/**
 * Interfaz que define los métodos que darán servicio a Suplemento
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 27/04/2024
 */
public interface SuplementoInterfaz {

	/**
	 * Método que obtiene todos los suplementos de la base de datos y los devuelve
	 * como DTO
	 * 
	 * @return Devuelve una lista de objetos SuplementoDTO
	 */
	public List<SuplementoDTO> obtieneTodosLosSuplementos();

	/**
	 * Método que obtiene un suplemento por su id
	 * 
	 * @param id_suplemento Id del suplemento a obtener
	 * @return Devuelve un objeto de tipo SuplementoDTO
	 */
	public SuplementoDTO obtieneSuplementoPorId(long id_suplemento);

	/**
	 * Método que borra un suplemento por su id.
	 * 
	 * @param id_suplemento Id del suplemento a eliminar
	 * @return Devuelve true si se ha eliminado o false si no se ha eliminado.
	 */
	public boolean borraSuplementoPorId(long id_suplemento);

	/**
	 * Método que actualiza un suplemento
	 * 
	 * @param suplementoDTO Objeto SuplementoDTO con los nuevos datos del suplemento
	 * @return Devuelve true si se ha actualizado o false si no.
	 */
	public boolean actualizaSuplemento(SuplementoDTO suplementoDTO);

	/**
	 * Método que agrega un nuevo suplemento a la base de datos
	 * 
	 * @param suplementoDTO Objeto SuplementoDTO que se va a agregar
	 * @return Devuelve true si se ha agregado o false si no.
	 */
	public boolean agregaSuplemento(SuplementoDTO suplementoDTO);
}
