package com.example.demo.daos;

import java.util.Calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad Token que representa la tabla tokens de la base de datos
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tokens", schema = "gtp_usuarios")
public class Token {

	// Atributos

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_token;
	@Column(name = "cod_token", nullable = false)
	private String cod_token;
	@Column(name = "fch_fin_token", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar fch_fin_token;
	@ManyToOne
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
}
