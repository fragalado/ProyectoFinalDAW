package com.example.demo.servicios;

import java.util.List;

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
			// Obtenemos el usuario por el email
			UsuarioDTO usuarioDTO = usuarioImplementacion.obtieneUsuarioPorEmail(emailUsuario);

			// Ahora que tenemos el usuario vamos a obtener el carrito
			List<Carrito> listaCarritoDAO = carritoRepositorio.findAllCarritoNoComprado(Util.usuarioADao(usuarioDTO));

			// Convertimos la lista a DTO
			List<CarritoDTO> listaCarritoDTO = Util.listaCarritoDaoADto(listaCarritoDAO);

			// Devolvemos la lista
			return listaCarritoDTO;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean borraCarrito(long id_carrito) {
		try {
			// Borramos el carrito
			carritoRepositorio.deleteById(id_carrito);

			// Ahora buscamos el carrito para comprobar si se ha borrado
			if (!carritoRepositorio.findById(id_carrito).isEmpty())
				return false; // Se ha encontrado, luego no se ha borrado

			return true; // No se ha encontrado el carrito, luego se ha borrado
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	@Override
	public boolean agregaSuplemento(long id_suplemento, String email_usuario) {
		try {
			// Obtenemos el usuario por el email
			UsuarioDTO usuarioDTO = usuarioImplementacion.obtieneUsuarioPorEmail(email_usuario);

			// Obtenemos el suplemento por el id
			SuplementoDTO suplementoDTO = suplementoImplementacion.obtieneSuplementoPorId(id_suplemento);

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
			return false;
		} catch (OptimisticLockingFailureException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public float obtienePrecioTotalCarrito(List<CarritoDTO> listaCarrito) {
		try {
			// Obtenemos el precio total
			float total = 0;
			for (CarritoDTO aux : listaCarrito)
				total += aux.getCantidad() * aux.getSuplementoDTO().getPrecioSuplemento();

			// Devolvemos el total
			return (float) (Math.round(total * 100.0) / 100.0);
		} catch (Exception e) {
			return 0f;
		}
	}
}
