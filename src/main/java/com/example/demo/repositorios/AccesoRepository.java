package com.example.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.daos.Acceso;

/**
 * Repositorio para realizar el CRUD de Acceso
 * 
 * @author Francisco José Gallego Dorado
 */
@Repository
public interface AccesoRepository extends JpaRepository<Acceso, Long> {

}
