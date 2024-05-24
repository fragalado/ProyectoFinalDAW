package com.example.demo.daos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Suplemento que hace referencia a la tabla suplementos de la base de datos
 * 
 * @author Francisco José Gallego Dorado
 * Fecha: 27/04/2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "suplementos")
public class Suplemento {

	// Atributos
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idSuplemento;
	
	@Column(name = "nombre_suplemento", nullable = false)
	private String nombreSuplemento;
	
	@Column(name = "desc_suplemento", nullable = false)
	private String descSuplemento;
	
	@Column(name = "precio_suplemento", nullable = false)
	private float precioSuplemento;
	
	@Column(name = "tipo_suplemento", nullable = false)
	private String tipoSuplemento;
	
	@Column(name = "marca_suplemento", nullable = false)
	private String marcaSuplemento;
	
	@Column(name = "imagen_suplemento", nullable = false, columnDefinition = "LONGBLOB")
	private byte[] imagenSuplemento;
}
