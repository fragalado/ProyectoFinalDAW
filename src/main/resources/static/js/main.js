// Validación formulario
function validarFormulario() {
	// Obtenemos los datos a validar
	let email = document.getElementById('email').value;
	let password = document.getElementById('password').value;
	let telefono = document.getElementById('tlf').value;
	let imagen = document.getElementById("imagenForm").value.split('.')[1];

	// Variables donde guardaremos el error
	let emailError = validarEmail(email);
	let passwordError = validarPassword(password);
	let telefonoError = validarTelefono(telefono);
	let imagenError = validarImagen(imagen);
	

	// Mostramos los errores
	if (emailError)
		muestraToast(emailError, false);
	if (passwordError)
		muestraToast(passwordError, false);
	if (telefonoError)
		muestraToast(telefonoError, false);
	if(imagenError != "")
		muestraToast(imagenError, false);

	// Comprobamos si se ha obtenido algun error o no
	return emailError === "" && passwordError === "" && telefonoError === "" && imagenError === "";
}

// Validar formulario registro
function validarFormularioRegistro() {
	// Obtenemos los datos a validar
	let email = document.getElementById('email').value;
	let password = document.getElementById('password').value;
	let telefono = document.getElementById('tlf').value;

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

// Validar formulario editar usuario desde admin
function validarFormularioEditarAdmin() {
	// Obtenemos los datos a validar
	let email = document.getElementById('email').value;
	let telefono = document.getElementById('tlf').value;
	let imagen = document.getElementById("imagenForm").value.split('.')[1];

	// Variables donde guardaremos el error
	let emailError = validarEmail(email);
	let telefonoError = validarTelefono(telefono);
	let imagenError = validarImagen(imagen);

	// Mostramos los errores
	if(emailError)
		muestraToast(emailError, false);
	if(telefonoError)
		muestraToast(telefonoError, false);
	if(imagenError != "")
		muestraToast(imagenError, false);

	// Comprobamos si se ha obtenido algun error o no
	return emailError === "" && telefonoError === "" && imagenError === "";
}

// Validar formulario editar perfil
function validarFormularioEditarPerfil() {
	// Obtenemos los datos a validar
	let telefono = document.getElementById('tlf').value;
	let imagen = document.getElementById("imagenForm").value.split('.')[1];

	// Variables donde guardaremos el error
	let telefonoError = validarTelefono(telefono);
	let imagenError = validarImagen(imagen);

	// Mostramos los errores
	if(telefonoError)
		muestraToast(telefonoError, false);
	if(imagenError != "")
		muestraToast(imagenError, false);

	// Comprobamos si se ha obtenido algun error o no
	return telefonoError === "" && imagenError === "";
}

// Validar passwords en el formulario de modificar password
function validarPasswords() {
	// Obtenemos las dos password
	let password1 = document.getElementById("password1").value;
	let password2 = document.getElementById("password2").value;

	// Comprobamos si son iguales
	if (password1 != password2) {
		muestraToast("Las contraseñas tienen que coincidir.", false);
		return false;
	} else if (password1.length < 6 || password2.length < 6) {
		muestraToast("La contraseña debe tener al menos 6 caracteres.")
		return false;
	} else if(password1.includes(' ') || password2.includes(' ')) {
		muestraToast("La contraseña no puede contener espacios en blanco.")
		return false;
	} else {
		return true;
	}
}

// Métodos para validar email, password, telefono e imagen

function validarEmail(email) {
	if (!email) {
		return "Por favor ingresa un email.";
	} else if (!/\S+@\S+\.\S+/.test(email)) {
		return "Por favor ingresa un email válido.";
	} else if (email.includes(' ')){
		return "El email no puede contener espacios en blanco.";
	}
	return ""; // Email válido
}

function validarPassword(password) {
	if (!password) {
		return "Por favor ingresa una contraseña.";
	} else if (password.length < 6) {
		return "La contraseña debe tener al menos 6 caracteres.";
	} else if (password.includes(' ')) {
		return "La contraseña no puede contener espacios en blanco."
	}
	return ""; // Contraseña válida
}

function validarTelefono(telefono) {
	// Expresión regular para validar un número de teléfono de 9 dígitos
    const regexTelefono = /^[0-9]{9}$/;
	
	if (!telefono) {
		return "Por favor ingresa un número de teléfono.";
	} else if (!regexTelefono.test(telefono)) {
		return "Por favor ingresa un número de teléfono válido (9 dígitos).";
	} else if (telefono.includes(' ')){
		return "El teléfono no puede contener espacios en blanco."
	}
	return ""; // Teléfono válido
}

function validarImagen(tipoImagen){
	// Comprobamos el tipo de imagen
	if(tipoImagen == "jpg" || tipoImagen == "png" || tipoImagen == "jpeg" || tipoImagen == "webp")
		return "";
	else
		return "El formato de la imagen no es válido. Formatos válidos: jpg, png, jpeg, webp."
}