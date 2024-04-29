package com.example.demo.servicios;

import java.util.Calendar;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.config.EmailProperties;
import com.example.demo.daos.Token;
import com.example.demo.daos.Usuario;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Implementación de la interfaz Email
 * 
 * @author Francisco José Gallego Dorado 
 * Fecha: 21/04/2024
 */
@Service
public class EmailImplementacion implements EmailInterfaz {

	@Autowired
	private JavaMailSender mailSender; // Interfaz de Spring para enviar correos

	@Autowired
	private TokenImplementacion tokenImplementacion;
	
	@Autowired
	private EmailProperties appProperties;

	@Override
	public boolean enviarEmail(String direccion, boolean esActivarCuenta, Usuario usuario) {

		try {
			// Generamos un token
			UUID uuid = UUID.randomUUID();
			String token = uuid.toString();

			// Creamos la fecha limite para añadir al token
			Calendar fecha = Calendar.getInstance();
			fecha.add(Calendar.MINUTE, 10); // 10 minutos

			// Creamos el token y lo guardamos en la base de datos
			Token tokenDao = new Token(0, token, fecha, usuario);
			boolean ok = tokenImplementacion.guardaToken(tokenDao);

			// Comprobamos si se ha guardado el token
			// Si no se ha guardado no enviaremos el correo
			if (ok) {
				// Enviamos el correo
				String asuntoEmail = "";
				if (esActivarCuenta) {
					asuntoEmail = "Activar cuenta";
				} else {
					asuntoEmail = "Modificar contraseña";
				}

				// Creamos un objeto MimeMessage en vez de SimpleMailMessage para poder poner
				// estilos
				MimeMessage message = mailSender.createMimeMessage();
				// Creamos un objeto MimeMessageHelper para facilitar la creacion del mensaje.
				// Pasamos el objeto MimeMessage, true para habilitar la opcion de contenido
				// mixto y la codificacion utf-8
				MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
				helper.setFrom(appProperties.getEmailFrom());
				helper.setTo(usuario.getEmail_usuario());
				helper.setSubject(asuntoEmail);
				helper.setText(mensajeCorreo(tokenDao.getCod_token(), direccion, esActivarCuenta), true);

				// Enviamos el correo utilizando el JavaMailSender
				mailSender.send(message);
				return true;
			} else {
				return false;
			}

		} catch (IllegalArgumentException e) {
			return false;
		} catch (OptimisticLockingFailureException e) {
			return false;
		} catch (MailAuthenticationException e) {
			return false;
		} catch (MailSendException e) {
			return false;
		} catch (MessagingException e) {
			return false;
		}
	}

	/**
	 * Método que devuelve el cuerpo/body del correo a enviar.
	 * 
	 * @param token      Token creado
	 * @param direccion  Direccion pagina web
	 * @param esActivado Boolean para controlar si es mensaje de recuperacion o
	 *                   activacion de cuenta.
	 * @return
	 */
	private String mensajeCorreo(String token, String direccion, boolean esActivado) {
		if (esActivado) {
			return String.format(
					"""
							<div style="font-family: 'Optima', sans-serif; max-width: 600px; margin: 0 auto; color: #192255; line-height: 1.6;">
							    <h2 style="color: #192255; font-size: 24px; font-weight: bold; text-transform: uppercase; margin-bottom: 20px; text-align: left;">Confirmar cuenta</h2>

							    <p style="font-size: 16px; text-align: left; margin-bottom: 30px;">
							        Se ha enviado una petición para activar la cuenta. Si has sido tú, haz clic en el siguiente botón para poder activar la cuenta:
							    </p>

							    <a href="%s?tk=%s" style="text-decoration: none;" target="_blank">
							        <button style="background-color: #285845; color: white; padding: 15px 25px; border: none; border-radius: 5px; font-size: 18px; cursor: pointer; text-transform: uppercase;">
							            Activar Cuenta
							        </button>
							    </a>
							</div>
							""",
					direccion, token);
		} else {
			return String.format(
					"""
							<div style="font-family: 'Optima', sans-serif; max-width: 600px; margin: 0 auto; color: #192255; line-height: 1.6;">
							    <h2 style="color: #192255; font-size: 24px; font-weight: bold; text-transform: uppercase; margin-bottom: 20px; text-align: left;">Restablecer Contraseña</h2>

							    <p style="font-size: 16px; text-align: left; margin-bottom: 30px;">
							        Se ha enviado una petición para restablecer la contraseña. Si no has sido tú, por favor cambia la contraseña inmediatamente.
							        Si has sido tú, haz clic en el siguiente botón para restablecer tu contraseña:
							    </p>

							    <a href="%s?tk=%s" style="text-decoration: none;" target="_blank">
							        <button style="background-color: #285845; color: white; padding: 15px 25px; border: none; border-radius: 5px; font-size: 18px; cursor: pointer; text-transform: uppercase;">
							            Restablecer Contraseña
							        </button>
							    </a>
							</div>
							""",
					direccion, token);
		}
	}

}
