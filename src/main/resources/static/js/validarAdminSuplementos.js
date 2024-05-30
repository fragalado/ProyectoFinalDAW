function validarFormulario() {
	// Obtenemos los datos el formulario
	let precio = document.getElementById("precioForm").value.trim();
	let tipo = document.getElementById("tipoForm").value.trim();
	let imagen = document.getElementById("imagenForm").value.split('.')[1];
	
	// Validamos los campos
	let precioError = validarPrecio(precio);
	if (precioError != "")
		muestraToast(precioError, false);

	let tipoError = validarTipo(tipo);
	if (tipoError != "")
		muestraToast(tipoError, false);

	let imagenError = validarImagen(imagen);
	if(imagenError != "")
		muestraToast(imagenError, false);

	if (tipoError || precioError || imagenError)
		return false;
	return true;
}

function validarPrecio(precio) {
	if (precio <= 0 || precio > 999) {
		return "El precio tiene que estar entre 1 y 999";
	} else
		return "";
}

function validarTipo(tipo) {
	// Comprobamos que no este vacio, si esta vacio quiere decir que no se ha seleccionado ninguna opcion
	if (tipo == "") {
		return "Tienes que seleccionar un tipo";
	} else {
		return "";
	}
}

function validarImagen(tipoImagen){
	// Comprobamos el tipo de imagen
	if(tipoImagen == "jpg" || tipoImagen == "png" || tipoImagen == "jpeg" || tipoImagen == "webp")
		return "";
	else
		return "El formato de la imagen no es válido. Formatos válidos: jpg, png, jpeg, webp."
}