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
@Table(name = "suplementos", schema = "gtp_hechos")
public class Suplemento {

	// Atributos
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_suplemento;
	
	@Column(name = "nombre_suplemento", nullable = false)
	private String nombre_suplemento;
	
	@Column(name = "desc_suplemento", nullable = false)
	private String desc_suplemento;
	
	@Column(name = "precio_suplemento", nullable = false)
	private float precio_suplemento;
	
	@Column(name = "tipo_suplemento", nullable = false)
	private String tipo_suplemento;
	
	@Column(name = "marca_suplemento", nullable = false)
	private String marca_suplemento;
	
	@Column(name = "imagen_suplemento", nullable = false)
	private byte[] imagen_suplemento;
}
