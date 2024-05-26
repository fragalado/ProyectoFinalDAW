package com.example.demo.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.SuplementoDTO;
import com.example.demo.servicios.SuplementoImplementacion;
import com.example.demo.utiles.Util;

/**
 * Controlador que controla las peticiones HTTP para la ruta /admin/suplementos
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 28/04/2024
 */
@Controller
@RequestMapping("/admin/suplementos")
public class AdministracionSuplementosControlador {

	@Autowired
	private SuplementoImplementacion suplementoImplementacion;

	/**
	 * Método que controla las peticiones GET para la ruta /admin/suplementos
	 * 
	 * @param model Objeto Model para enviar datos a la vista
	 * @return Devuelve una vista
	 */
	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String vistaAdministracionSuplementos(Model model) {
		try {
			Util.logInfo("AdministracionSuplementosControlador", "vistaAdministracionSuplementos", "Ha entrado");
			// Obtenemos una lista con todos los suplementos y lo agregamos al modelo
			model.addAttribute("listaSuplementosDTO", suplementoImplementacion.obtieneTodosLosSuplementos());

			// Devolvemos la vista
			return "admin/suplementos/administracionSuplementos";
		} catch (Exception e) {
			Util.logError("AdministracionSuplementosControlador", "vistaAdministracionSuplementos",
					"Se ha producido un error");
			return "redirect:/home";
		}
	}

	/**
	 * Método que controla las peticiones GET para la ruta /admin/suplementos/editar/{id_suplemento}
	 * 
	 * @param id_suplemento Id del suplemento a editar
	 * @param model         Objeto Model para enviar datos a la vista
	 * @return Devuelve una vista
	 */
	@GetMapping("/editar/{id_suplemento}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String vistaEditarSuplemento(@PathVariable long id_suplemento, Model model) {
		try {
			Util.logInfo("AdministracionSuplementosControlador", "vistaEditarSuplemento", "Ha entrado");
			// Obtenemos el suplemento de la base de datos y lo agregamos al modelo
			SuplementoDTO suplementoDTO = suplementoImplementacion.obtieneSuplementoPorId(id_suplemento);

			// Lo agregamos al modelo
			model.addAttribute("suplementoDTO", suplementoDTO);

			// Devolvemos la vista
			return "admin/suplementos/editarSuplemento";
		} catch (Exception e) {
			Util.logError("AdministracionSuplementosControlador", "vistaEditarSuplemento", "Se ha producido un error");
			return "redirect:/home";
		}
	}

	/**
	 * Método que controla las peticiones GET para la ruta /admin/suplementos/agregar
	 * 
	 * @param model Objeto Model para enviar datos a la vista
	 * @return Devuelve una vista
	 */
	@GetMapping("/agregar")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String vistaAgregarSuplemento(Model model) {
		try {
			Util.logInfo("AdministracionSuplementosControlador", "vistaAgregarSuplemento", "Ha entrado");
			// Agregamos al modelo un objeto suplemento
			model.addAttribute("suplementoDTO", new SuplementoDTO());

			// Devolvemos la vista
			return "admin/suplementos/agregarSuplemento";
		} catch (Exception e) {
			Util.logError("AdministracionSuplementosControlador", "vistaAgregarSuplemento", "Se ha producido un error");
			return "redirect:/home";
		}
	}

	/**
	 * Método que controla las peticiones GET para la ruta /admin/suplementos/borrar/{id_suplemento}
	 * 
	 * @param id_suplemento Id del suplemento a borrar
	 * @return Devuelve una redirección
	 */
	@GetMapping("/borrar/{id_suplemento}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String borraSuplemento(@PathVariable long id_suplemento) {
		try {
			Util.logInfo("AdministracionSuplementosControlador", "borraSuplemento", "Ha entrado");
			// Eliminamos el suplemento por el id_suplemento
			boolean ok = suplementoImplementacion.borraSuplementoPorId(id_suplemento);

			// Devolvemos la vista con un parametro segun se haya elimando o no
			if (ok)
				return "redirect:/admin/suplementos?success";
			else
				return "redirect:/admin/suplementos?error";
		} catch (Exception e) {
			Util.logError("AdministracionSuplementosControlador", "borraSuplemento", "Se ha producido un error");
			return "redirect:/admin/suplementos?error";
		}
	}

	/**
	 * Método que controla las peticiones POST para la ruta /admin/suplementos/editar
	 * 
	 * @param suplementoDTO Objeto SuplementoDTO con los nuevos datos del suplemento
	 * @param imagenFile    Objeto MultipartFile con los datos del archivo
	 * @return Devuelve una redirección
	 */
	@PostMapping("/editar")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String editaSuplemento(@ModelAttribute("suplementoDTO") SuplementoDTO suplementoDTO,
			@RequestPart("imagenFile") MultipartFile imagenFile) {
		try {
			Util.logInfo("AdministracionSuplementosControlador", "editaSuplemento", "Ha entrado");
			// Controlamos los valores
			if (suplementoDTO.getPrecioSuplemento() > 999 || suplementoDTO.getNombreSuplemento().length() > 255
					|| suplementoDTO.getDescSuplemento().length() > 255
					|| suplementoDTO.getMarcaSuplemento().length() > 255
					|| suplementoDTO.getTipoSuplemento().length() > 255) {
				return "redirect:/admin/suplementos?suplementoEditadoError";
			}

			// Pasamos la imagen a String
			String foto = Util.convertirABase64(imagenFile.getBytes());

			// Le añadimos la imagen al suplementoDTO
			suplementoDTO.setImagenSuplemento(foto);

			// Actualizamos el suplemento
			boolean ok = suplementoImplementacion.actualizaSuplemento(suplementoDTO);

			if (ok)
				return "redirect:/admin/suplementos?suplementoEditadoSuccess";
			else
				return "redirect:/admin/suplementos?suplementoEditadoError";
		} catch (Exception e) {
			Util.logError("AdministracionSuplementosControlador", "editaSuplemento", "Se ha producido un error");
			return "redirect:/admin/suplementos?suplementoEditadoError";
		}
	}

	/**
	 * Método que controla las peticiones POST para la ruta /admin/suplementos/agregar
	 * 
	 * @param suplementoDTO Objeto SuplementoDTO con los datos del nuevo suplemento
	 *                      a agregar
	 * @param imagenFile    Objeto MultipartFile con los datos del archivo
	 * @return Devuelve una redirección
	 */
	@PostMapping("/agregar")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String agregaSuplemento(@ModelAttribute("suplementoDTO") SuplementoDTO suplementoDTO,
			@RequestPart("imagenFile") MultipartFile imagenFile) {
		try {
			Util.logInfo("AdministracionSuplementosControlador", "agregaSuplemento", "Ha entrado");
			// Controlamos los valores
			if (suplementoDTO.getPrecioSuplemento() > 999 || suplementoDTO.getNombreSuplemento().length() > 255
					|| suplementoDTO.getDescSuplemento().length() > 255
					|| suplementoDTO.getMarcaSuplemento().length() > 255
					|| suplementoDTO.getTipoSuplemento().length() > 255) {
				return "redirect:/admin/suplementos?suplementoAgregadoError";
			}

			// Pasamos la imagen a String
			String foto = Util.convertirABase64(imagenFile.getBytes());

			// Le añadimos la imagen al suplementoDTO
			suplementoDTO.setImagenSuplemento(foto);

			// Agregamos el suplemento a la base de datos
			boolean ok = suplementoImplementacion.agregaSuplemento(suplementoDTO);

			if (ok)
				return "redirect:/admin/suplementos?suplementoAgregadoSuccess";
			else
				return "redirect:/admin/suplementos?suplementoAgregadoError";
		} catch (Exception e) {
			Util.logError("AdministracionSuplementosControlador", "agregaSuplemento", "Se ha producido un error");
			return "redirect:/admin/suplementos?suplementoAgregadoError";
		}
	}
}
