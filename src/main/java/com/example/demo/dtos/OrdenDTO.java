package com.example.demo.dtos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import lombok.Data;

/**
 * DTO para Orden
 * 
 * Fecha: 25/05/2024
 * 
 * @author Francisco Jose Gallego Dorado
 */
@Data
public class OrdenDTO {

	// Atributos
	
	private int idOrden;
	private float precioOrden;
	private Calendar fchOrden;
	private List<CarritoDTO> listaCarritoDto = new ArrayList<>();
	
	public String parseaFecha() {
		int day = fchOrden.get(Calendar.DAY_OF_MONTH);
		int month = fchOrden.get(Calendar.MONTH) + 1;
		int year = fchOrden.get(Calendar.YEAR);
		String formattedDate = String.format("%02d/%02d/%d", day, month, year);
		return formattedDate;
	}
}
