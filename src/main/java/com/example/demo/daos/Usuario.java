package com.example.demo.daos;

import java.util.List;

import jakarta.persistence.CascadeType;
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
 * Entidad Usuario que representa a la tabla usuarios de la base de datos
 * 
 * @author Francisco José Gallego Dorado Fecha: 21/04/2024
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

	// Atributos

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idUsuario;
	@Column(name = "nombre_usuario", nullable = false, length = 50)
	private String nombreUsuario;
	@Column(name = "tlf_usuario", nullable = false)
	private String tlfUsuario;
	@Column(name = "email_usuario", nullable = false, length = 50)
	private String emailUsuario;
	@Column(name = "psswd_usuario", nullable = false)
	private String psswdUsuario;
	@ManyToOne
	@JoinColumn(name = "id_acceso")
	private Acceso acceso;
	@Column(name = "esta_activado_usuario", nullable = false)
	private boolean estaActivadoUsuario;
	@Column(name = "imagen_usuario", nullable = true, columnDefinition = "LONGBLOB")
	private byte[] imagenUsuario;
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE)
	private List<Token> listaTokens;
	@OneToMany(mappedBy = "usuario")
	private List<Orden> listaOrden;
}
