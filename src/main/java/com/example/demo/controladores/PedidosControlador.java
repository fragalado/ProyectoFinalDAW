package com.example.demo.controladores;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dtos.CarritoDTO;
import com.example.demo.dtos.OrdenDTO;
import com.example.demo.servicios.CarritoImplementacion;
import com.example.demo.servicios.OrdenImplementacion;
import com.example.demo.utiles.Util;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador que controla las peticiones HTTP para la ruta /pedidos
 * 
 * Fecha: 24/05/2024
 * 
 * @author Francisco Jose Gallego Dorado
 */
@Controller
@RequestMapping("/pedidos")
public class PedidosControlador {

	@Autowired
	private OrdenImplementacion ordenImplementacion;

	@Autowired
	private CarritoImplementacion carritoImplementacion;

	/**
	 * Método que controla las peticiones GET para la ruta /pedidos
	 * 
	 * @param modelo         Objeto Model para enviar datos a la vista
	 * @param authentication Objeto Authentication con los datos de la autenticación
	 * @return Devuelve una vista
	 */
	@GetMapping
	public String vistaPedidos(Model modelo, Authentication authentication) {

		try {
			Util.logInfo("PedidosControlador", "vistaPedidos", "Ha entrado");

			// Obtenemos todos los pedidos del usuario
			List<OrdenDTO> listaOrdenDto = ordenImplementacion.obtieneComprasUsuario(authentication.getName());

			// Añadimos la lista al modelo
			modelo.addAttribute("listaOrdenDto", listaOrdenDto);

			// Obtenemos el numero de carrito del usuario
			modelo.addAttribute("tieneCarrito",
					carritoImplementacion.obtieneCantidadDeCarritosUsuario(authentication.getName()));

			// Devolvemos la vista
			return "pedidos/pedidos";
		} catch (Exception e) {
			Util.logError("PedidosControlador", "vistaPedidos", "Se ha producido un error");
			return "redirect:/errorVista";
		}
	}

	/**
	 * Método que controla las peticiones GET para la ruta /pedidos/export/{id}
	 * 
	 * @param idOrden  Id del orden a exportar a PDF
	 * @param response Objeto HttpServletResponse para personalizar la respuesta
	 */
	@GetMapping("/export/{id}")
	public void exportPedido(@PathVariable("id") long idOrden, HttpServletResponse response) {

		try {
			// Log
			Util.logInfo("PedidosControlador", "exportPedido", "Ha entrado");

			// Obtenemos el orden por el id
			OrdenDTO ordenDto = ordenImplementacion.obtieneOrdenPorId(idOrden);

			// Comprobamos si es distinto de null
			if (ordenDto != null) {
				response.setContentType("application/pdf");
				String headerKey = "Content-Disposition";
				// Obtenemos la fecha y la formateamos
				Calendar hoy = Calendar.getInstance();
				String fechaFormateada = hoy.get(Calendar.DAY_OF_MONTH) + "-" + (hoy.get(Calendar.MONTH) + 1) + "-"
						+ hoy.get(Calendar.YEAR);
				String headerValue = "attachment; filename=" + fechaFormateada + ".pdf";
				response.setHeader(headerKey, headerValue);

				Document document = new Document(PageSize.A4);
				PdfWriter.getInstance(document, response.getOutputStream());

				// Añadimos el footer
				HeaderFooter footer = new HeaderFooter(true, new Phrase(" page"));
				footer.setAlignment(Element.ALIGN_CENTER);
				footer.setBorderWidthBottom(0);
				// Cargamos la imagen del logo de manera más robusta
				Image imagen2 = Image.getInstance(getClass().getResource("/static/img/logo2.png"));
				imagen2.scaleToFit(35, 35);
				footer.addSpecialContent(imagen2);
				document.setFooter(footer);

				document.open();

				// Añadimos un título al documento
				Font font = new Font(Font.HELVETICA, 16, Font.BOLD);
				Paragraph title = new Paragraph("Factura Pedido", font);
				title.setAlignment(Paragraph.ALIGN_CENTER);
				document.add(title);

				// Añadimos una línea en blanco
				document.add(new Paragraph("\n"));

				// Creamos una tabla con 4 columnas
				PdfPTable table = new PdfPTable(4);
				table.setWidthPercentage(100);
				table.setSpacingBefore(10f);
				table.setSpacingAfter(10f);

				// Definimos los anchos de las columnas
				float[] columnWidths = { 1f, 3f, 2f, 2f };
				table.setWidths(columnWidths);

				// Añadimos el encabezado de la tabla
				Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
				PdfPCell headerCell;

				headerCell = new PdfPCell(new Paragraph("#", headerFont));
				table.addCell(headerCell);

				headerCell = new PdfPCell(new Paragraph("Suplemento", headerFont));
				table.addCell(headerCell);

				headerCell = new PdfPCell(new Paragraph("Cantidad", headerFont));
				table.addCell(headerCell);

				headerCell = new PdfPCell(new Paragraph("Precio unidad", headerFont));
				table.addCell(headerCell);

				// Añadimos los datos a la tabla
				int contador = 1;
				for (CarritoDTO carritoDto : ordenDto.getListaCarritoDto()) {
					table.addCell(String.valueOf(contador));
					table.addCell(carritoDto.getSuplementoDTO().getNombreSuplemento());
					table.addCell(String.valueOf(carritoDto.getCantidad()));
					table.addCell(String.valueOf(carritoDto.getSuplementoDTO().getPrecioSuplemento()));
					contador++;
				}

				document.add(table);
				document.close();
			}
		} catch (Exception e) {
			// Log
			Util.logError("PedidosControlador", "exportPedido", "Se ha producido un error al generar el PDF.");
		}
	}
}
