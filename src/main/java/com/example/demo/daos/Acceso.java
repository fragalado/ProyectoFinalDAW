package com.example.demo.daos;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Acceso que representa la tabla Accesos de la base de datos
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Accesos", schema = "gtp_usuarios")
public class Acceso {

	// Atributos

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_acceso;
	@Column(name = "cod_acceso", nullable = false)
	private String cod_acceso;
	@Column(name = "desc_acceso", nullable = false)
	private String desc_acceso;
	@OneToMany(mappedBy = "acceso")
	private List<Usuario> listaUsuarios;
	
	// Constructores
	
	// Constructor con todos los parámetros -> Lombok
	// Constructor sin parámetros -> Lombok
	// Constructor sin lista usuario ->
	public Acceso(long id_acceso, String cod_acceso, String desc_acceso) {
		this.id_acceso = id_acceso;
		this.cod_acceso = cod_acceso;
		this.desc_acceso = desc_acceso;
	}
}
