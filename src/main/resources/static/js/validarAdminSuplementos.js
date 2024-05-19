function validarFormulario() {
	// Obtenemos los datos el formulario
	let precio = document.getElementById("precioForm").value.trim();
	let tipo = document.getElementById("tipoForm").value.trim();

	// Validamos los campos
	let precioError = validarPrecio(precio);
	if (precioError != "")
		muestraToast(precioError, false);
	let tipoError = validarTipo(tipo);
	if (tipoError != "")
		muestraToast(tipoError, false);

	if (tipoError || precioError)
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