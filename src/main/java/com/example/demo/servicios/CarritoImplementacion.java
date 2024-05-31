package com.example.demo.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.example.demo.daos.Carrito;
import com.example.demo.dtos.CarritoDTO;
import com.example.demo.dtos.SuplementoDTO;
import com.example.demo.dtos.UsuarioDTO;
import com.example.demo.repositorios.CarritoRepository;
import com.example.demo.utiles.Util;

/**
 * Implementación de la interfaz Carrito
 * 
 * Fecha: 15/05/2024
 * 
 * @author Francisco José Gallego Dorado
 */
@Service
public class CarritoImplementacion implements CarritoInterfaz {

	@Autowired
	private UsuarioImplementacion usuarioImplementacion;

	@Autowired
	private SuplementoImplementacion suplementoImplementacion;

	@Autowired
	private CarritoRepository carritoRepositorio;

	@Override
	public List<CarritoDTO> obtieneCarritoUsuario(String emailUsuario) {
		try {
			// Log
			Util.logInfo("CarritoImplementacion", "obtieneCarritoUsuario", "Ha entrado");

			// Obtenemos el usuario por el email
			UsuarioDTO usuarioDTO = usuarioImplementacion.obtieneUsuarioPorEmail(emailUsuario);

			// Ahora que tenemos el usuario vamos a obtener el carrito
			List<Carrito> listaCarritoDAO = carritoRepositorio.findAllCarritoNoComprado(Util.usuarioADao(usuarioDTO));

			// Convertimos la lista a DTO
			List<CarritoDTO> listaCarritoDTO = Util.listaCarritoDaoADto(listaCarritoDAO);

			// Devolvemos la lista
			return listaCarritoDTO;
		} catch (Exception e) {
			// Log
			Util.logError("CarritoImplementacion", "obtieneCarritoUsuario", "Se ha producido un error.");
			return null;
		}
	}

	@Override
	public boolean borraCarrito(long idCarrito, String emailUsuario) {
		try {
			// Log
			Util.logInfo("CarritoImplementacion", "borraCarrito", "Ha entrado");

			// Obtenemos el carrito por el id
			Optional<Carrito> carritoDao = carritoRepositorio.findById(idCarrito);

			// Comprobamos si se ha encontrado
			if (carritoDao.isEmpty())
				return false;

			// Si se ha encontrado comprobamos que el usuario que elimina el carrito es el
			// dueño del carrito.
			if (carritoDao.get().getUsuario().getEmailUsuario().equals(emailUsuario)) {
				// Borramos el carrito
				carritoRepositorio.deleteById(idCarrito);

				// Ahora buscamos el carrito para comprobar si se ha borrado
				if (!carritoRepositorio.findById(idCarrito).isEmpty())
					return false; // Se ha encontrado, luego no se ha borrado
				
				return true; // No se ha encontrado el carrito, luego se ha borrado
			} else {
				// No es el dueño del carrito
				return false;
			}
		} catch (IllegalArgumentException e) {
			// Log
			Util.logError("CarritoImplementacion", "borraCarrito", "Se ha producido un error.");

			return false;
		}
	}

	@Override
	public boolean agregaSuplemento(long idSuplemento, String emailUsuario) {
		try {
			// Log
			Util.logInfo("CarritoImplementacion", "agregaSuplemento", "Ha entrado");

			// Obtenemos el usuario por el email
			UsuarioDTO usuarioDTO = usuarioImplementacion.obtieneUsuarioPorEmail(emailUsuario);

			// Obtenemos el suplemento por el id
			SuplementoDTO suplementoDTO = suplementoImplementacion.obtieneSuplementoPorId(idSuplemento);

			// Comprobamos si el el usuario o el suplemento es null
			if (usuarioDTO == null || suplementoDTO == null) {
				// Log
				Util.logInfo("CarritoImplementacion", "agregaSuplemento", "El usuario o suplemento es null.");
				// Devolvemos false
				return false;
			}

			// Comprobamos si ya existe un carrito con el suplemento
			Carrito carrito = carritoRepositorio.findCarritoBySuplemento(Util.usuarioADao(usuarioDTO),
					Util.suplementoDtoADao(suplementoDTO));

			if (carrito == null) {
				// Creamos el carrito y lo agregamos
				Carrito carritoDAO = new Carrito();
				carritoDAO.setUsuario(Util.usuarioADao(usuarioDTO));
				carritoDAO.setCantidad(1);
				carritoDAO.setSuplemento(Util.suplementoDtoADao(suplementoDTO));

				// Agregamos el carrito
				carritoRepositorio.save(carritoDAO);
			} else {
				// Le aumentamos la cantidad en 1
				carrito.setCantidad(carrito.getCantidad() + 1);

				// Actualizamos el carrito
				carritoRepositorio.save(carrito);
			}

			return true; // Devolvemos true
		} catch (IllegalArgumentException e) {
			// Log
			Util.logError("CarritoImplementacion", "agregaSuplemento",
					"Se ha pasado un argumento ilegal o inapropiado.");

			return false;
		} catch (OptimisticLockingFailureException e) {
			// Log
			Util.logError("CarritoImplementacion", "agregaSuplemento",
					"Se ha producido un error OptimisticLockingFailure.");

			return false;
		} catch (Exception e) {
			// Log
			Util.logError("CarritoImplementacion", "agregaSuplemento", "Se ha producido un error.");

			return false;
		}
	}

	@Override
	public float obtienePrecioTotalCarrito(List<CarritoDTO> listaCarrito) {
		try {
			// Log
			Util.logInfo("CarritoImplementacion", "obtienePrecioTotalCarrito", "Ha entrado");

			// Obtenemos el precio total
			float total = 0;
			for (CarritoDTO aux : listaCarrito)
				total += aux.getCantidad() * aux.getSuplementoDTO().getPrecioSuplemento();

			// Devolvemos el total
			return (float) (Math.round(total * 100.0) / 100.0);
		} catch (Exception e) {
			// Log
			Util.logError("CarritoImplementacion", "obtienePrecioTotalCarrito", "Se ha producido un error.");

			return 0f;
		}
	}

	@Override
	public int obtieneCantidadDeCarritosUsuario(String emailUsuario) {
		try {
			// Log
			Util.logInfo("CarritoImplementacion", "obtieneCantidadDeCarritosUsuario", "Ha entrado");

			// Obtenemos todos los carritos del usuario
			List<CarritoDTO> listaCarritoDto = obtieneCarritoUsuario(emailUsuario);

			// Devolvemos el numero de carritos del usuario
			return listaCarritoDto.size();
		} catch (Exception e) {
			// Log
			Util.logError("CarritoImplementacion", "obtieneCantidadDeCarritosUsuario", "Se ha producido un error.");

			return 0;
		}
	}
}
