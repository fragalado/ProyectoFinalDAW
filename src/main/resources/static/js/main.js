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
	if (emailError)
		muestraToast(emailError, false);
	if (passwordError)
		muestraToast(passwordError, false);
	if (telefonoError)
		muestraToast(telefonoError, false);

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
	muestraToast(emailError, false);
	muestraToast(telefonoError, false);

	// Comprobamos si se ha obtenido algun error o no
	return emailError === "" && telefonoError === "";
}

function validarFormularioEditarPerfil() {
	// Obtenemos los datos a validar
	let telefono = document.getElementById('tlf').value.trim();

	// Variables donde guardaremos el error
	let telefonoError = validarTelefono(telefono);

	// Mostramos los errores
	muestraToast(telefonoError, false);

	// Comprobamos si se ha obtenido algun error o no
	return telefonoError === "";
}

function validarPasswords() {
	// Obtenemos las dos password
	let password1 = document.getElementById("password1").value.trim();
	let password2 = document.getElementById("password2").value.trim();

	// Comprobamos si son iguales
	if (password1 != password2) {
		muestraToast("Las contraseñas tienen que coincidir.", false);
		return false;
	} else if (password1.length < 6 || password2.length < 6) {
		muestraToast("La contraseña debe tener al menos 6 caracteres.")
		return false;
	} else {
		return true;
	}
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