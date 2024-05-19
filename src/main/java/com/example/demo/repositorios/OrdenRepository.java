package com.example.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.daos.Orden;

/**
 * Repositorio para realizar el CRUD de Orden
 * 
 * Fecha: 19/05/2024
 * @author Francisco José Gallego Dorado
 */
public interface OrdenRepository extends JpaRepository<Orden, Long> {

}
