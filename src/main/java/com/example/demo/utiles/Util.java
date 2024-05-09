package com.example.demo.utiles;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.example.demo.daos.Acceso;
import com.example.demo.daos.Suplemento;
import com.example.demo.daos.Usuario;
import com.example.demo.dtos.SuplementoDTO;
import com.example.demo.dtos.UsuarioDTO;

/**
 * Clase Util que contiene los métodos que se usarán muchas veces en toda la
 * aplicación
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
public class Util {

	private static ModelMapper modelMapper = new ModelMapper();
	
	/**
	 * Método que convierte un array de byte a String
	 * 
	 * @param datos Array de bytes
	 * @return Devuelve el el array de bytes convertido a string
	 */
	public static String convertirABase64(byte[] datos) {
		if (datos != null && datos.length > 0)
			return Base64.getEncoder().encodeToString(datos);

		return null;
	}

	/**
	 * Método que convierte un String a un array de bytes
	 * 
	 * @param base64 String
	 * @return Devuelve el string convertido a un array de bytes
	 */
	public static byte[] convertirAByteArray(String base64) {
		if (base64 != null && !base64.isEmpty())
			return Base64.getDecoder().decode(base64);

		return null;
	}
	
	/**
	 * Método que convierte un UsuarioDTO a DAO
	 * 
	 * @return Devuelve el usuario convertido a DAO
	 */
	public static Usuario usuarioADao(UsuarioDTO usuarioDto) {
		try {
			// Convertimos el usuarioDto a usuarioDao
			Usuario usuarioDao = modelMapper.map(usuarioDto, Usuario.class);
			if (usuarioDto.getId_acceso() == 1)
				usuarioDao.setAcceso(new Acceso(1, "Usu", "Usuarios de la tienda"));
			else
				usuarioDao.setAcceso(new Acceso(2, "Admin", "Administrador de la tienda"));
			// Comprobamos si tiene imagen
			if(usuarioDto.getImagen_usuario() != null) {
				// Tiene imagen luego se la añadimos
				usuarioDao.setImagen_usuario(convertirAByteArray(usuarioDto.getImagen_usuario()));
			}

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
			UsuarioDTO usuarioDto = modelMapper.map(usuarioDao, UsuarioDTO.class);
			usuarioDto.setId_acceso(usuarioDao.getAcceso().getId_acceso());
			// Comprobamos si tiene imagen
			if(usuarioDao.getImagen_usuario() != null) {
				// Tiene imagen, luego se la añadimos
				usuarioDto.setImagen_usuario(convertirABase64(usuarioDao.getImagen_usuario()));
			}

			// Devolvemos el usuarioDto
			return usuarioDto;
		} catch (Exception e) {
			// Error al convertir
			System.out.println("[Error-Util-usuarioADto] Error al convertir el Usuario a DTO");
			return null;
		}
	}
	
	public static List<UsuarioDTO> listaUsuarioDaoADto(List<Usuario> listaUsuarioDao){
		try {
			// Lista donde guardaremos los objetos DTOs
			List<UsuarioDTO> listaUsuarioDto = new ArrayList<>();
			
			// Recorremos la lista Dao
			for (Usuario usuarioDao : listaUsuarioDao) {
				UsuarioDTO usuarioDto = modelMapper.map(usuarioDao, UsuarioDTO.class);
				usuarioDto.setImagen_usuario(convertirABase64(usuarioDao.getImagen_usuario()));
				usuarioDto.setId_acceso(usuarioDao.getAcceso().getId_acceso());
				// Añadimos a la lista
				listaUsuarioDto.add(usuarioDto);
			}
			
			// Devolvemos la lista Dto
			return listaUsuarioDto;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Método que convierte una lista de objetos Suplemento Dao a Dto
	 * 
	 * @param listaSuplementoDao Lista de objetos de tipo Suplemento Dao
	 * @return Devuelve una lista con objetos de tipo Suplemento Dto
	 */
	public static List<SuplementoDTO> listaSuplementoDaoADto(List<Suplemento> listaSuplementoDao) {
		try {
			// Lista donde guardaremos los objetos DTOs
			List<SuplementoDTO> listaSuplementoDto = new ArrayList<>();

			// Recorremos la lista Dao
			for (Suplemento suplementoDao : listaSuplementoDao) {
				SuplementoDTO suplementoDTO = modelMapper.map(suplementoDao, SuplementoDTO.class);
				// Le añadimos la imagen
				suplementoDTO.setImagen_suplemento(convertirABase64(suplementoDao.getImagen_suplemento()));
				listaSuplementoDto.add(suplementoDTO);
			}

			// Devolvemos la lista dto
			return listaSuplementoDto;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Método que convierte una objeto de tipo Suplemento Dao a Dto
	 * 
	 * @param suplementodDao Objeto de tipo Suplemento Dao a convertir a Dto
	 * @return Devuelve un objeto de tipo Suplemento Dto
	 */
	public static SuplementoDTO suplementoDaoADto(Suplemento suplementodDao) {
		try {
			SuplementoDTO suplementoDTO = modelMapper.map(suplementodDao, SuplementoDTO.class);
			// Le añadimos la imagen
			suplementoDTO.setImagen_suplemento(convertirABase64(suplementodDao.getImagen_suplemento()));
			return suplementoDTO;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Método que convierte un suplemento de tipo Dto a Dao
	 * @param suplementoDTO Objeto SuplementoDto a convertir a Dao
	 * @return Devuelve un objeto de tipo Suplemento Dao
	 */
	public static Suplemento suplementoDtoADao(SuplementoDTO suplementoDTO) {
		try {
			Suplemento suplemento = modelMapper.map(suplementoDTO, Suplemento.class);
			// Le añadimos la imagen
			suplemento.setImagen_suplemento(convertirAByteArray(suplementoDTO.getImagen_suplemento()));
			return suplemento;
		} catch (Exception e) {
			return null;
		}
	}
}
