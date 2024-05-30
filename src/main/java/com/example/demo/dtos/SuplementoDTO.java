package com.example.demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase SuplementoDTO
 * 
 * @author Francisco José Gallego Dorado
 * Fecha: 27/04/2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuplementoDTO {

	// Atributos
	
	private long idSuplemento;
	private String nombreSuplemento;
	private String descSuplemento;
	private float precioSuplemento;
	private String tipoSuplemento;
	private String marcaSuplemento;
	private String imagenSuplemento;

	// Métodos

	public String formateaPrecio(){
		return String.format("%.2f", precioSuplemento);
	}
}
