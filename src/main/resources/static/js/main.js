// Validación formulario registro
function validarFormulario() {
	// Obtenemos los datos a validar
	let email = document.getElementById('email').value.trim();
	let password = document.getElementById('password').value.trim();
	let telefono = document.getElementById('tlf').value.trim();

	// Variables donde guardaremos el error
	let emailError = validarEmail(email);
	let passwordError = validarPassword(password);
	let telefonoError = validarTelefono(telefono);

	// Mostramos los errores
	mostrarError('emailError', emailError);
	mostrarError('passwordError', passwordError);
	mostrarError('telefonoError', telefonoError);

	// Comprobamos si se ha obtenido algun error o no
	return emailError === "" && passwordError === "" && telefonoError === "";
}

function validarFormularioEditarAdmin() {
	// Obtenemos los datos a validar
	let email = document.getElementById('email').value.trim();
	let telefono = document.getElementById('tlf').value.trim();

	// Variables donde guardaremos el error
	let emailError = validarEmail(email);
	let telefonoError = validarTelefono(telefono);

	// Mostramos los errores
	mostrarError('emailError', emailError);
	mostrarError('telefonoError', telefonoError);

	// Comprobamos si se ha obtenido algun error o no
	return emailError === "" && telefonoError === "";
}

function validarFormularioEditarPerfil() {
	// Obtenemos los datos a validar
	let telefono = document.getElementById('tlf').value.trim();

	// Variables donde guardaremos el error
	let telefonoError = validarTelefono(telefono);

	// Mostramos los errores
	mostrarError('telefonoError', telefonoError);

	// Comprobamos si se ha obtenido algun error o no
	return telefonoError === "";
}

function validarEmail(email) {
	if (!email) {
		return "Por favor ingresa un email.";
	} else if (!/\S+@\S+\.\S+/.test(email)) {
		return "Por favor ingresa un email válido.";
	}
	return ""; // Email válido
}

function validarPassword(password) {
	if (!password) {
		return "Por favor ingresa una contraseña.";
	} else if (password.length < 6) {
		return "La contraseña debe tener al menos 6 caracteres.";
	}
	return ""; // Contraseña válida
}

function validarTelefono(telefono) {
	if (!telefono) {
		return "Por favor ingresa un número de teléfono.";
	} else if (telefono.length !== 9) {
		return "Por favor ingresa un número de teléfono válido (9 dígitos).";
	}
	return ""; // Teléfono válido
}

function mostrarError(elementId, error) {
	let elemento = document.getElementById(elementId);
	elemento.textContent = error;
	elemento.style.display = error ? "" : "none";
}