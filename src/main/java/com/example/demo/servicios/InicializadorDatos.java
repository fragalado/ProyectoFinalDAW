package com.example.demo.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.daos.Acceso;
import com.example.demo.daos.Usuario;
import com.example.demo.repositorios.AccesoRepository;
import com.example.demo.repositorios.UsuarioRepository;

import jakarta.annotation.PostConstruct;

/**
 * Inicializador de datos
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 29/04/2024
 */
@Component
public class InicializadorDatos {

	@Autowired
	private AccesoRepository accesoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostConstruct
	public void init() {
		try {
			// Comprobamos si la tabla acceso no tiene datos
			if (accesoRepository.count() == 0) {
				// Si no tiene le añadimos
				Acceso accesoUsuario = new Acceso(1, "User", "Usuario de la tienda");
				Acceso accesoAdmin = new Acceso(2, "Admin", "Administrador de la tienda");

				// Lo agregamos a la base de datos
				accesoRepository.save(accesoUsuario);
				accesoRepository.save(accesoAdmin);
			}

			// Comprobamos si no existe ningun administrador en la base de datos
			if (usuarioRepository.count() == 0) {
				// Si no hay ningun admin añadimos uno
				Usuario usuarioAdmin = new Usuario(0, "Admin", "123456789", "admin@gmail.com",
						bCryptPasswordEncoder.encode("adminAltair123"),
						new Acceso(2, "Admin", "Administrador de la tienda"), true, null, null, null, null);

				// Lo añadimos
				usuarioRepository.save(usuarioAdmin);
			}
		} catch (Exception e) {
			System.out.println("[Error-InicializadorDatos-init] No se ha podido inicializar datos.");
		}
	}
}
