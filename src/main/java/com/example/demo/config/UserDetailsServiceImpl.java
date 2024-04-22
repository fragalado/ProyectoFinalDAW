package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.daos.Usuario;
import com.example.demo.repositorios.UsuarioRepository;

/**
 * Implementación de la interfaz UserDetailsService
 * 
 * @author Francisco José Gallego Dorado
 * Fecha: 21/04/2024
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepositorio;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Obtenemos el usuario de la base de datos por el email
		Usuario usuarioEncontrado = usuarioRepositorio.findByEmailUsuario(username);

		// Comprobamos si no se ha encontrado
		if (usuarioEncontrado == null) {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}

		// Comprobamos si el usuario está desactivado
		if (!usuarioEncontrado.isEstaActivado_usuario()) {
			throw new DisabledException("Usuario desactivado");
		}

		// Contruimos un objeto UserDetails con los datos
		return User.builder()
				.username(username)
				.password(usuarioEncontrado.getPsswd_usuario())
				.disabled(false)
				.authorities("ROLE_" + usuarioEncontrado.getAcceso().getCod_acceso().toUpperCase())
				.build();
	}

}
