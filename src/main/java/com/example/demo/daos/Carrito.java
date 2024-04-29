package com.example.demo.daos;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Carrito que hace referencia a la tabla carritos de la base de datos
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 28/04/2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carritos", schema = "gtp_hechos")
public class Carrito {

	// Atributos

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_carrito;

	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "id_suplemento")
	private Suplemento suplemento;

	@Column(name = "cantidad", nullable = false)
	private int cantidad;

	@Column(name = "estaComprado_carrito")
	private boolean estaComprado_carrito;

	@OneToMany(mappedBy = "carrito")
	private List<RelOrdenCarrito> listaRelacion;
}
