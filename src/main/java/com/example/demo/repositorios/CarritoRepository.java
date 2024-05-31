package com.example.demo.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.daos.Carrito;
import com.example.demo.daos.Suplemento;
import com.example.demo.daos.Usuario;

/**
 * Repositorio para realizar el CRUD de Carrito
 * 
 * Fecha: 15/05/2024
 * 
 * @author Francisco José Gallego Dorado
 */
public interface CarritoRepository extends JpaRepository<Carrito, Long> {

	/**
	 * Método que devuelve una lista de tipo Carrito con todos los carritos que no
	 * estén comprados
	 * 
	 * @param usuario Objeto Usuario
	 * @return Devuelve una lista de tipo Carrito List<Carrito>
	 */
	@Query("SELECT c FROM Carrito c WHERE c.usuario=:usuario AND c.estaCompradoCarrito = false")
	public List<Carrito> findAllCarritoNoComprado(@Param("usuario") Usuario usuario);

	/**
	 * Método que devuelve un carrito (DAO) por su usuario y el suplemento y si no
	 * esta comprado
	 * 
	 * @param usuario    Usuario del carrito
	 * @param suplemento Suplemento del carrito
	 * @return Devuelve el carrito obtenido
	 */
	@Query("SELECT c FROM Carrito c WHERE c.usuario=:usuario AND c.suplemento=:suplemento AND c.estaCompradoCarrito = false")
	public Carrito findCarritoBySuplemento(@Param("usuario") Usuario usuario,
			@Param("suplemento") Suplemento suplemento);
}
