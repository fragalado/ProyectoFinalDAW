package com.example.demo.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.daos.Orden;

/**
 * Repositorio para realizar el CRUD de Orden
 * 
 * Fecha: 19/05/2024
 * 
 * @author Francisco José Gallego Dorado
 */
public interface OrdenRepository extends JpaRepository<Orden, Long> {

	/**
	 * Método que encuentra todas las ordenes de un usuario por el email
	 * 
	 * @param emailUsuario Email del usuario
	 * @return List<Orden>
	 */
	@Query("SELECT or FROM Orden or WHERE or.usuario.emailUsuario=?1")
	public List<Orden> findOrdenByEmailUsuario(String emailUsuario);
}
