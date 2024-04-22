package com.example.demo.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.daos.Token;

/**
 * Repositorio que dará servicio a la entidad Token
 * 
 * @author Francisco José Gallego Dorado
 * Fecha: 21/04/2024
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

	/**
	 * Método que obtiene un token por su código
	 * @param codToken Código del token a obtener
	 * @return Devuelve el token obtenido
	 */
	@Query("SELECT t FROM Token t WHERE t.cod_token = :codToken")
	public Token findByCodToken(@Param("codToken") String codToken);
}
