package com.example.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.daos.Suplemento;

/**
 * Repositorio para la entidad Suplemento, repositorio que contiene los métodos para hacer CRUD sobre Suplememento
 * 
 * @author Francisco José Gallego Dorado
 * Fecha: 27/04/2024
 */
@Repository
public interface SuplementoRepository extends JpaRepository<Suplemento, Long>{

}
