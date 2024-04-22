package com.example.demo.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.example.demo.daos.Token;
import com.example.demo.repositorios.TokenRepository;

import jakarta.transaction.Transactional;

/**
 * Implementación de la interfaz Token
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
@Service
@Transactional
public class TokenImplementacion implements TokenInterfaz {

	@Autowired
	private TokenRepository tokenRepositorio;

	@Override
	public boolean guardaToken(Token token) {
		try {
			// Guardamos el token
			Token tokenDevuelto = tokenRepositorio.save(token);

			// Comprobamos si se ha guardado correctamente
			if (tokenDevuelto != null)
				return true; // Devolvemos true si se ha guardado correctamente

			return false; // Devolvemos false si no se ha guardado
		} catch (IllegalArgumentException e) {
			return false; // Devolvemos false en caso de error
		} catch (OptimisticLockingFailureException e) {
			return false; // Devolvemos false en caso de error
		}
	}

	@Override
	public Token obtieneToken(String token) {
		try {
			// Obtenemos el token de la base de datos
			Token tokenEncontrado = tokenRepositorio.findByCodToken(token);

			// Comprobamos si es distinto de null
			if (tokenEncontrado != null) {
				return tokenEncontrado; // Devolvemos el token obtenido
			}

			return null; // Devolvemos null si no se ha encontrado el token
		} catch (Exception e) {
			return null; // Devolvemos null en caso de error
		}
	}

}
