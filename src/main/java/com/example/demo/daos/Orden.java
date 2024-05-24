package com.example.demo.daos;

import java.util.Calendar;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Orden que hace referencia a la tabla ordenes de la base de datos
 * 
 * @author Francisco José Gallego Dorado Fecha: 28/04/2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ordenes")
public class Orden {

	// Atributos

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idOrden;

	@Column(name = "precio_orden", nullable = false)
	private float precioOrden;

	@Column(name = "fch_orden", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar fchOrden;

	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	@OneToMany(mappedBy = "orden")
	private List<RelOrdenCarrito> listaRelacion;
}
