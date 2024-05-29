package com.example.demo.repositorios;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.daos.Usuario;

/**
 * Repositorio que dará servicio a la entidad Usuario
 * 
 * @author Francisco José Gallego Dorado
 * Fecha: 21/04/2024
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	/**
	 * Método que busca un usuario por el email
	 * @param email Email del usuario a obtener
	 * @return Devuelve el usuario obtenido
	 */
	@Query("SELECT u FROM Usuario u WHERE u.emailUsuario = :email")
	public Usuario findByEmailUsuario(@Param("email") String email);
	
	@Query ("select count(u) from Usuario u where u.acceso.codAcceso = 'Admin'")
	public int countAdminUsers();

	@Query("select u from Usuario u where u.emailUsuario like %?1%")
	public List<Usuario> findAllUsuariosByKeyword(String keyword);
}
