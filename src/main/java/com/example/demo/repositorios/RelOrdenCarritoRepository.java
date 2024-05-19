package com.example.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.daos.RelOrdenCarrito;

/**
 * Repositorio para realizar el CRUD de RelOrdenCarrito
 * 
 * Fecha: 19/05/2024
 * @author Francisco José Gallego Dorado
 */
public interface RelOrdenCarritoRepository extends JpaRepository<RelOrdenCarrito, Long> {

}
