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
			// Obtenemos todos los suplementos de la base de datos y lo guardamos en una lista de tipo Suplemento (DAO)
			List<Suplemento> listaSuplementosDao = suplementoRepository.findAll();
			
			// Pasamos de DAO a DTO
			List<SuplementoDTO> listaSuplementosDTO = Util.listaSuplementoDaoADto(listaSuplementosDao);
			
			// Devolvemos la lista de suplementos DTO
			return listaSuplementosDTO;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public SuplementoDTO obtieneSuplementoPorId(long id_suplemento) {
		try {
			// Obtenemos el suplemento
			Optional<Suplemento> suplementoEncontrado = suplementoRepository.findById(id_suplemento);
			
			// Comprobamos si no se ha encontrado el suplemento
			if(suplementoEncontrado.isEmpty())
				return null;
			
			// Si se ha encontrado vamos a convertir el suplemento de DAO a DTO
			SuplementoDTO suplementoDTO = Util.suplementoDaoADto(suplementoEncontrado.get());
			
			// Devolvemos el suplemento convertido a DTO
			return suplementoDTO;
		} catch (IllegalArgumentException e) {
			return null;
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	@Override
	public boolean borraSuplementoPorId(long id_suplemento) {
		try {
			// Comprobamos si existe un suplemento con el id pasado
			Optional<Suplemento> suplementoEncontrado = suplementoRepository.findById(id_suplemento);
			
			// Comprobamos si no existe
			if(suplementoEncontrado.isEmpty())
				return false;
			
			// Si existe lo eliminamos por el id
			suplementoRepository.deleteById(id_suplemento);
			
			// Ahora para comprobar si se ha eliminado vamos a buscar el suplemento por el id
			Optional<Suplemento> suplementoEliminado = suplementoRepository.findById(id_suplemento);
			
			if(suplementoEliminado.isEmpty())
				return true; // Devolvemos true si no existe
			
			return false; // En caso de que se haya encontrado un suplemento con el id
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	@Override
	public boolean actualizaSuplemento(SuplementoDTO suplementoDTO) {
		try {
			System.out.println(suplementoDTO.getId_suplemento());
			// Con el id del suplemento pasado obtenemos el suplemento de la base de datos
			Suplemento suplementoEncontrado = suplementoRepository.findById(suplementoDTO.getId_suplemento()).get();
			
			// Actualizamos algunos datos del suplementoEncontrado con el suplementoDTO
			suplementoEncontrado.setNombre_suplemento(suplementoDTO.getNombre_suplemento());
			suplementoEncontrado.setDesc_suplemento(suplementoDTO.getDesc_suplemento());
			suplementoEncontrado.setMarca_suplemento(suplementoDTO.getMarca_suplemento());
			suplementoEncontrado.setTipo_suplemento(suplementoDTO.getTipo_suplemento());
			suplementoEncontrado.setPrecio_suplemento(suplementoDTO.getPrecio_suplemento());
			if(suplementoDTO.getImagen_suplemento() != null)
				suplementoEncontrado.setImagen_suplemento(Util.convertirAByteArray(suplementoDTO.getImagen_suplemento()));
			
			// Actualizamos el suplemento
			suplementoRepository.save(suplementoEncontrado);
			
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		} catch (OptimisticLockingFailureException e) {
			return false;
		}
	}

	@Override
	public boolean agregaSuplemento(SuplementoDTO suplementoDTO) {
		try {
			// Convertimos el suplemento de DTO a DAO
			Suplemento suplementoDAO = Util.suplementoDtoADao(suplementoDTO);
			
			// Agregamos el suplemento a la base de datos
			Suplemento suplementoDevuelto = suplementoRepository.save(suplementoDAO);
			
			// Comprobamos si se ha añadido
			if(suplementoDevuelto != null)
				return true; // Se ha añadido correctamente
			
			return false; // Si se ha producido algún error
		} catch (IllegalArgumentException e) {
			return false;
		} catch (OptimisticLockingFailureException e) {
			return false;
		}
	}

}
