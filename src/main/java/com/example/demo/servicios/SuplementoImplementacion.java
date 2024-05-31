package com.example.demo.servicios;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.example.demo.daos.Suplemento;
import com.example.demo.dtos.SuplementoDTO;
import com.example.demo.repositorios.SuplementoRepository;
import com.example.demo.utiles.Util;

/**
 * Implementación de la interfaz Suplemento
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 27/04/2024
 */
@Service
public class SuplementoImplementacion implements SuplementoInterfaz {

	@Autowired
	private SuplementoRepository suplementoRepository;

	@Override
	public List<SuplementoDTO> obtieneTodosLosSuplementos() {
		try {
			// Log
			Util.logInfo("SuplementoImplementacion", "obtieneTodosLosSuplementos", "Ha entrado");

			// Obtenemos todos los suplementos de la base de datos y lo guardamos en una
			// lista de tipo Suplemento (DAO)
			List<Suplemento> listaSuplementosDao = suplementoRepository.findAll();

			// Pasamos de DAO a DTO
			List<SuplementoDTO> listaSuplementosDTO = Util.listaSuplementoDaoADto(listaSuplementosDao);

			// Devolvemos la lista de suplementos DTO
			return listaSuplementosDTO;
		} catch (Exception e) {
			// Log
			Util.logError("SuplementoImplementacion", "obtieneTodosLosSuplementos", "Se ha producido un error.");

			return null;
		}
	}

	@Override
	public SuplementoDTO obtieneSuplementoPorId(long idSuplemento) {
		try {
			// Log
			Util.logInfo("SuplementoImplementacion", "obtieneSuplementoPorId", "Ha entrado");

			// Obtenemos el suplemento
			Optional<Suplemento> suplementoEncontrado = suplementoRepository.findById(idSuplemento);

			// Comprobamos si no se ha encontrado el suplemento
			if (suplementoEncontrado.isEmpty())
				return null;

			// Si se ha encontrado vamos a convertir el suplemento de DAO a DTO
			SuplementoDTO suplementoDTO = Util.suplementoDaoADto(suplementoEncontrado.get());

			// Devolvemos el suplemento convertido a DTO
			return suplementoDTO;
		} catch (IllegalArgumentException e) {
			// Log
			Util.logError("SuplementoImplementacion", "obtieneSuplementoPorId",
					"Se ha intentado pasar un argumento ilegal o inapropiado a un método.");

			return null;
		} catch (NoSuchElementException e) {
			// Log
			Util.logError("SuplementoImplementacion", "obtieneSuplementoPorId",
					"Se ha intentado pasar un argumento que no existe.");

			return null;
		}
	}

	@Override
	public boolean borraSuplementoPorId(long idSuplemento) {
		try {
			// Log
			Util.logInfo("SuplementoImplementacion", "borraSuplementoPorId", "Ha entrado");

			// Comprobamos si existe un suplemento con el id pasado
			Optional<Suplemento> suplementoEncontrado = suplementoRepository.findById(idSuplemento);

			// Comprobamos si no existe
			if (suplementoEncontrado.isEmpty())
				return false;

			// Si existe lo eliminamos por el id
			suplementoRepository.deleteById(idSuplemento);

			// Ahora para comprobar si se ha eliminado vamos a buscar el suplemento por el
			// id
			Optional<Suplemento> suplementoEliminado = suplementoRepository.findById(idSuplemento);

			if (suplementoEliminado.isEmpty())
				return true; // Devolvemos true si no existe

			return false; // En caso de que se haya encontrado un suplemento con el id
		} catch (IllegalArgumentException e) {
			// Log
			Util.logError("SuplementoImplementacion", "borraSuplementoPorId",
					"Se ha intentado pasar un argumento ilegal o inapropiado a un método.");

			return false;
		}
	}

	@Override
	public boolean actualizaSuplemento(SuplementoDTO suplementoDTO) {
		try {
			// Log
			Util.logInfo("SuplementoImplementacion", "actualizaSuplemento", "Ha entrado");

			// Con el id del suplemento pasado obtenemos el suplemento de la base de datos
			Suplemento suplementoEncontrado = suplementoRepository.findById(suplementoDTO.getIdSuplemento()).get();

			// Actualizamos algunos datos del suplementoEncontrado con el suplementoDTO
			suplementoEncontrado.setNombreSuplemento(suplementoDTO.getNombreSuplemento().trim());
			suplementoEncontrado.setDescSuplemento(suplementoDTO.getDescSuplemento().trim());
			suplementoEncontrado.setMarcaSuplemento(suplementoDTO.getMarcaSuplemento().trim());
			suplementoEncontrado.setTipoSuplemento(suplementoDTO.getTipoSuplemento().trim());
			suplementoEncontrado.setPrecioSuplemento(suplementoDTO.getPrecioSuplemento());
			if (suplementoDTO.getImagenSuplemento() != null)
				suplementoEncontrado.setImagenSuplemento(Util.convertirAByteArray(suplementoDTO.getImagenSuplemento()));

			// Actualizamos el suplemento
			suplementoRepository.save(suplementoEncontrado);

			return true;
		} catch (IllegalArgumentException e) {
			// Log
			Util.logError("SuplementoImplementacion", "actualizaSuplemento",
					"Se ha intentado pasar un argumento ilegal o inapropiado a un método.");

			return false;
		} catch (OptimisticLockingFailureException e) {
			// Log
			Util.logError("SuplementoImplementacion", "actualizaSuplemento",
					"Se ha producido un error OptimisticLockingFailure.");

			return false;
		} catch (NoSuchElementException e) {
			// Log
			Util.logError("SuplementoImplementacion", "actualizaSuplemento",
					"Se ha intentado pasar un argumento que no existe a un método.");

			return false;
		}
	}

	@Override
	public boolean agregaSuplemento(SuplementoDTO suplementoDTO) {
		try {
			// Log
			Util.logInfo("SuplementoImplementacion", "agregaSuplemento", "Ha entrado");

			// Convertimos el suplemento de DTO a DAO
			Suplemento suplementoDAO = Util.suplementoDtoADao(suplementoDTO);

			// Agregamos el suplemento a la base de datos
			Suplemento suplementoDevuelto = suplementoRepository.save(suplementoDAO);

			// Comprobamos si se ha añadido
			if (suplementoDevuelto != null)
				return true; // Se ha añadido correctamente

			return false; // Si se ha producido algún error
		} catch (IllegalArgumentException e) {
			// Log
			Util.logError("SuplementoImplementacion", "agregaSuplemento",
					"Se ha intentado pasar un argumento ilegal o inapropiado a un método.");

			return false;
		} catch (OptimisticLockingFailureException e) {
			// Log
			Util.logError("SuplementoImplementacion", "agregaSuplemento",
					"Se ha producido un error OptimisticLockingFailure.");

			return false;
		}
	}

	@Override
	public List<SuplementoDTO> obtieneSuplementosPorKeyword(String keyword) {
		try {
			// Log
			Util.logInfo("SuplementoImplementacion", "obtieneSuplementosPorKeyword", "Ha entrado");

			// Obtenemos los suplementos por la keyword
			List<Suplemento> listaSuplementosDao = suplementoRepository.findAllSuplementosByKeyword(keyword);

			// Si es distinto de null la convertimos a DTO y devolvemos
			if (listaSuplementosDao != null)
				return Util.listaSuplementoDaoADto(listaSuplementosDao);
			// Si es null devolvemos null
			return null;
		} catch (Exception e) {
			// Log
			Util.logError("SuplementoImplementacion", "obtieneSuplementosPorKeyword", "Se ha producido un error.");

			return null;
		}
	}

}
