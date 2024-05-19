package com.example.demo.servicios;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.daos.Carrito;
import com.example.demo.daos.Orden;
import com.example.demo.daos.RelOrdenCarrito;
import com.example.demo.daos.Usuario;
import com.example.demo.dtos.CarritoDTO;
import com.example.demo.repositorios.CarritoRepository;
import com.example.demo.repositorios.OrdenRepository;
import com.example.demo.repositorios.RelOrdenCarritoRepository;
import com.example.demo.utiles.Util;

/**
 * Implementación de la interfaz Orden
 * 
 * Fecha: 19/05/2024
 * 
 * @author Francisco José Gallego Dorado
 */
public class OrdenImplementacion implements OrdenInterfaz {

	@Autowired
	private OrdenRepository ordenRepositorio;

	@Autowired
	private RelOrdenCarritoRepository ordenCarritoRepositorio;

	@Autowired
	private CarritoRepository carritoRepositorio;

	@Autowired
	private UsuarioImplementacion usuarioImplementacion;

	@Autowired
	private CarritoImplementacion carritoImplementacion;

	@Override
	public boolean compraCarritoUsuario(String emailUsuario) {
		try {
			// Obtenemos el usuario
			Usuario usuarioDAO = Util.usuarioADao(usuarioImplementacion.obtieneUsuarioPorEmail(emailUsuario));

			// Obtenemos los carritos del usuario
			List<CarritoDTO> listaCarritos = carritoImplementacion.obtieneCarritoUsuario(emailUsuario);

			// Obtenemos el precio total del carrito
			float precioTotal = carritoImplementacion.obtienePrecioTotalCarrito(listaCarritos);

			// Creamos un objeto Orden
			Orden orden = new Orden();
			orden.setPrecio_orden(precioTotal);
			orden.setUsuario(usuarioDAO);
			orden.setFch_orden(Calendar.getInstance());

			// Guardamos la orden
			Orden ordenDevuelta = ordenRepositorio.save(orden);

			// Ahora vamos a guardar los datos en la tabla intermedia
			for (CarritoDTO aux : listaCarritos) {
				// Convertimos el carrito a DAO
				Carrito carritoDAO = new Carrito();
				carritoDAO.setId_carrito(aux.getId_carrito());
				carritoDAO.setEstaComprado_carrito(aux.isEstaComprado_carrito());
				carritoDAO.setCantidad(aux.getCantidad());
				carritoDAO.setSuplemento(Util.suplementoDtoADao(aux.getSuplementoDTO()));
				// Obtenemos el usuario por el id
				carritoDAO.setUsuario(Util.usuarioADao(usuarioImplementacion.obtieneUsuarioPorId(aux.getId_usuario())));

				// Ahora que tenemos el carrito en DAO creamos el objeto RelOrdenCarrito
				RelOrdenCarrito ordenCarrito = new RelOrdenCarrito();
				ordenCarrito.setCarrito(carritoDAO);
				ordenCarrito.setOrden(ordenDevuelta);

				// Hacemos el insert en la tabla intermedia
				ordenCarritoRepositorio.save(ordenCarrito);

				// Ahora hacemos el update del carrito
				carritoDAO.setEstaComprado_carrito(true);
				carritoRepositorio.save(carritoDAO);
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
