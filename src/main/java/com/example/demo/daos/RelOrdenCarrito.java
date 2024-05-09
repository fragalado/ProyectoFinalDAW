package com.example.demo.daos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad RelOrdenCarrito que hace referencia a la tabla rel_ordenes_carritos
 * de la base de datos
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 28/04/2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rel_ordenes_carritos", schema = "gtp_hechos")
public class RelOrdenCarrito {

	// Atributos

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_rel_orden_carrito;

	@ManyToOne
	@JoinColumn(name = "id_orden")
	private Orden orden;

	@ManyToOne
	@JoinColumn(name = "id_carrito")
	private Carrito carrito;
}
