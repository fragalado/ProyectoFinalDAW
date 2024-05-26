package com.example.demo.utiles;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.demo.daos.Acceso;
import com.example.demo.daos.Carrito;
import com.example.demo.daos.Orden;
import com.example.demo.daos.RelOrdenCarrito;
import com.example.demo.daos.Suplemento;
import com.example.demo.daos.Usuario;
import com.example.demo.dtos.CarritoDTO;
import com.example.demo.dtos.OrdenDTO;
import com.example.demo.dtos.SuplementoDTO;
import com.example.demo.dtos.UsuarioDTO;

/**
 * Clase Util que contiene los métodos que se usarán muchas veces en toda la
 * aplicación
 * 
 * @author Francisco José Gallego Dorado Fecha: 21/04/2024
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
			if (usuarioDto.getIdAcceso() == 1)
				usuarioDao.setAcceso(new Acceso(1, "Usu", "Usuarios de la tienda"));
			else
				usuarioDao.setAcceso(new Acceso(2, "Admin", "Administrador de la tienda"));
			// Comprobamos si tiene imagen
			if (usuarioDto.getImagenUsuario() != null) {
				// Tiene imagen luego se la añadimos
				usuarioDao.setImagenUsuario(convertirAByteArray(usuarioDto.getImagenUsuario()));
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
			usuarioDto.setIdAcceso(usuarioDao.getAcceso().getIdAcceso());
			// Comprobamos si tiene imagen
			if (usuarioDao.getImagenUsuario() != null) {
				// Tiene imagen, luego se la añadimos
				usuarioDto.setImagenUsuario(convertirABase64(usuarioDao.getImagenUsuario()));
			}

			// Devolvemos el usuarioDto
			return usuarioDto;
		} catch (Exception e) {
			// Error al convertir
			System.out.println("[Error-Util-usuarioADto] Error al convertir el Usuario a DTO");
			return null;
		}
	}

	public static List<UsuarioDTO> listaUsuarioDaoADto(List<Usuario> listaUsuarioDao) {
		try {
			// Lista donde guardaremos los objetos DTOs
			List<UsuarioDTO> listaUsuarioDto = new ArrayList<>();

			// Recorremos la lista Dao
			for (Usuario usuarioDao : listaUsuarioDao) {
				UsuarioDTO usuarioDto = modelMapper.map(usuarioDao, UsuarioDTO.class);
				usuarioDto.setImagenUsuario(convertirABase64(usuarioDao.getImagenUsuario()));
				usuarioDto.setIdAcceso(usuarioDao.getAcceso().getIdAcceso());
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
				suplementoDTO.setImagenSuplemento(convertirABase64(suplementoDao.getImagenSuplemento()));
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
			suplementoDTO.setImagenSuplemento(convertirABase64(suplementodDao.getImagenSuplemento()));
			return suplementoDTO;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Método que convierte un suplemento de tipo Dto a Dao
	 * 
	 * @param suplementoDTO Objeto SuplementoDto a convertir a Dao
	 * @return Devuelve un objeto de tipo Suplemento Dao
	 */
	public static Suplemento suplementoDtoADao(SuplementoDTO suplementoDTO) {
		try {
			Suplemento suplemento = modelMapper.map(suplementoDTO, Suplemento.class);
			// Le añadimos la imagen
			suplemento.setImagenSuplemento(convertirAByteArray(suplementoDTO.getImagenSuplemento()));
			return suplemento;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Método que convierte una lista de carrito de tipo Dao a Dto
	 * 
	 * @param listaCarritoDao Lista con objetos de tipo Carrito Dao
	 * @return Devuelve la lista convertida a Dto
	 */
	public static List<CarritoDTO> listaCarritoDaoADto(List<Carrito> listaCarritoDao) {
		try {
			// Lista donde guardaremos los objetos DTOs
			List<CarritoDTO> listaCarritoDto = new ArrayList<>();

			// Recorremos la lista Dao
			for (Carrito carritoDao : listaCarritoDao) {
				CarritoDTO carritoDTO = modelMapper.map(carritoDao, CarritoDTO.class);
				// Le añadimos el id del usuario
				carritoDTO.setIdUsuario(carritoDao.getUsuario().getIdUsuario());
				// Le añadimos el suplemento
				carritoDTO.setSuplementoDTO(suplementoDaoADto(carritoDao.getSuplemento()));
				listaCarritoDto.add(carritoDTO);
			}

			// Devolvemos la lista dto
			return listaCarritoDto;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Método que convierte una lista de orden de tipo Dao a Dto
	 * 
	 * @param listaOrdenDao Lista con objetos de tipo Orden Dao
	 * @return Devuelve una lista convertida a Dto
	 */
	public static List<OrdenDTO> listaOrdenDaoADto(List<Orden> listaOrdenDao) {
		try {
			// Lista donde guardaremos las orden DTOs
			List<OrdenDTO> listaOrdenDto = new ArrayList<>();

			// Recorremos la lista Dao
			for (Orden orden : listaOrdenDao) {
				// Mapeamos a OrdenDTO
				OrdenDTO ordenDto = modelMapper.map(orden, OrdenDTO.class);

				// Recorremos la lista relacion
				for (RelOrdenCarrito aux : orden.getListaRelacion()) {
					// Convertimos el carrito a DTO
					CarritoDTO carritoDTO = modelMapper.map(aux.getCarrito(), CarritoDTO.class);
					// Le añadimos el suplemento
					carritoDTO.setSuplementoDTO(suplementoDaoADto(aux.getCarrito().getSuplemento()));
					// Agregamos el carrito a la orden
					ordenDto.getListaCarritoDto().add(carritoDTO);
				}

				listaOrdenDto.add(ordenDto);
			}

			// Devolvemos la lista dto
			return listaOrdenDto;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Método para info que escribe en un fichero de texto
	 * 
	 * @param nombreClase  Nombre de la clase
	 * @param nombreMetodo Nombre del método
	 * @param mensaje      Mensaje a escribir en el fichero de texto
	 */
	public static void logInfo(String nombreClase, String nombreMetodo, String mensaje) {
		try {
			String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
			// Ruta de la carpeta
			String rutaCarpeta = "C:\\FicherosProg\\" + emailUsuario;
			// Comprobamos si existe la carpeta, si no existe la crea
			verificarYCrearCarpeta(rutaCarpeta);
			// Obtenemos el dia de hoy para ponerle de nombre al log
			Calendar hoy = Calendar.getInstance();
			String diaFormateado = hoy.get(Calendar.DAY_OF_MONTH) + "-" + hoy.get(Calendar.MONTH) + "-"
					+ hoy.get(Calendar.YEAR);
			FileWriter file = new FileWriter(rutaCarpeta + "\\" + diaFormateado + ".log", true);
			PrintWriter pw = new PrintWriter(file);
			pw.println("[" + LocalDateTime.now() + "]-[INFO-" + nombreClase + "-" + nombreMetodo + "] " + mensaje);
			pw.close();
			file.close();
		} catch (Exception e) {
			System.out.println("[ERROR-Util-logInfo] Error: no se ha podido escribir la info en el fichero log");
		}
	}

	/**
	 * Método para errores que escribe en un fichero de texto
	 * 
	 * @param nombreClase  Nombre de la clase
	 * @param nombreMetodo Nombre del método
	 * @param mensaje      Mensaje a escribir en el fichero de texto
	 */
	public static void logError(String nombreClase, String nombreMetodo, String mensaje) {
		try {
			String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
			// Ruta de la carpeta
			String rutaCarpeta = "C:\\FicherosProg\\" + emailUsuario;
			// Comprobamos si existe la carpeta, si no existe la crea
			verificarYCrearCarpeta(rutaCarpeta);
			// Obtenemos el dia de hoy para ponerle de nombre al log
			Calendar hoy = Calendar.getInstance();
			String diaFormateado = hoy.get(Calendar.DAY_OF_MONTH) + "-" + hoy.get(Calendar.MONTH) + "-"
					+ hoy.get(Calendar.YEAR);
			FileWriter file = new FileWriter(rutaCarpeta + "\\" + diaFormateado + ".log", true);
			PrintWriter pw = new PrintWriter(file);
			pw.println(
					"[" + LocalDateTime.now() + "]-[ERROR-" + nombreClase + "-" + nombreMetodo + "] Error: " + mensaje);
			pw.close();
			file.close();
		} catch (Exception e) {
			System.out.println("[ERROR-Util-logError] Error: no se ha podido escribir el error en el fichero log");
		}
	}

	/**
	 * Método que comprueba si existe una carpeta, si no existe la crea
	 * 
	 * @param nombreCarpeta Nombre de la carpeta
	 */
	private static void verificarYCrearCarpeta(String rutaCarpeta) {
		try {
			// Crea un objeto Path con la ruta de la carpeta
			Path path = Paths.get(rutaCarpeta);

			// Verifica si la carpeta existe
			if (Files.notExists(path)) {
				// Crea la carpeta incluyendo los directorios intermedios si no existen
				Files.createDirectories(path);
			}
		} catch (Exception e) {
			System.out.println("[ERROR-Util-verificarYCrearCarpeta] Error: no se ha podido crear la carpeta.");
		}
	}
}
