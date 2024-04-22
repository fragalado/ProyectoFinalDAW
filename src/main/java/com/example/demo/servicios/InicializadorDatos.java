package com.example.demo.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.daos.Acceso;
import com.example.demo.repositorios.AccesoRepository;

import jakarta.annotation.PostConstruct;

@Component
public class InicializadorDatos {

	@Autowired
	private AccesoRepository accesoRepository;
	
	@PostConstruct
	public void init() {
		// Comprobamos si hay datos en la tabla acceso
		if(accesoRepository.count() == 0) {
			// Si no hay datos agregamos datos
			accesoRepository.save(new Acceso(1, "User", "Usuario de la tienda"));
			accesoRepository.save(new Acceso(2, "Admin", "Administrador de la tienda"));
		}
	}
}
