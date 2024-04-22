package com.example.demo.utiles;

import com.example.demo.daos.Acceso;
import com.example.demo.daos.Usuario;
import com.example.demo.dtos.UsuarioDTO;

/**
 * Clase Util que contiene los métodos que se usarán muchas veces en toda la
 * aplicación
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
public class Util {

	/**
	 * Método que convierte un UsuarioDTO a DAO
	 * 
	 * @return Devuelve el usuario convertido a DAO
	 */
	public static Usuario usuarioADao(UsuarioDTO usuarioDto) {
		try {
			// Convertimos el usuarioDto a usuarioDao
			Usuario usuarioDao = new Usuario();
			usuarioDao.setId_usuario(usuarioDto.getId_usuario());
			usuarioDao.setEmail_usuario(usuarioDto.getEmail_usuario());
			usuarioDao.setNombre_usuario(usuarioDto.getNombre_usuario());
			usuarioDao.setTlf_usuario(usuarioDto.getTlf_usuario());
			usuarioDao.setEstaActivado_usuario(usuarioDto.isEstaActivado_usuario());
			usuarioDao.setPsswd_usuario(usuarioDto.getPsswd_usuario());
			if (usuarioDto.getId_acceso() == 1)
				usuarioDao.setAcceso(new Acceso(1, "Usu", "Usuarios de la tienda"));
			else
				usuarioDao.setAcceso(new Acceso(2, "Admin", "Administrador de la tienda"));

			// Devolvemos el usuarioDao
			return usuarioDao;
		} catch (Exception e) {
			// Error al convertir
			System.out.println("[Error-Util-usuarioADao] Error al convertir el Usuario a DAO");
			return null;
		}
	}

	/**
	 * Método que convierte un Usuario (DAO) a DTO
	 * 
	 * @return Devuelve el usuario convertido a DTO
	 */
	public static UsuarioDTO usuarioADto(Usuario usuarioDao) {
		try {
			// Convertimos el usuarioDao a usuarioDto
			UsuarioDTO usuarioDto = new UsuarioDTO();
			usuarioDto.setId_usuario(usuarioDao.getId_usuario());
			usuarioDto.setEmail_usuario(usuarioDao.getEmail_usuario());
			usuarioDto.setNombre_usuario(usuarioDao.getNombre_usuario());
			usuarioDto.setTlf_usuario(usuarioDao.getTlf_usuario());
			usuarioDto.setEstaActivado_usuario(usuarioDao.isEstaActivado_usuario());
			usuarioDto.setPsswd_usuario(usuarioDao.getPsswd_usuario());
			usuarioDto.setId_acceso(usuarioDao.getAcceso().getId_acceso());

			// Devolvemos el usuarioDto
			return usuarioDto;
		} catch (Exception e) {
			// Error al convertir
			System.out.println("[Error-Util-usuarioADto] Error al convertir el Usuario a DTO");
			return null;
		}
	}
}
