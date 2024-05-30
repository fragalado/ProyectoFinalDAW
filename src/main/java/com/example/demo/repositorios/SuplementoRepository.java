package com.example.demo.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("select s from Suplemento s where s.nombreSuplemento like %?1%")
    public List<Suplemento> findAllSuplementosByKeyword(String keyword);
}
