function muestraToast(mensaje, esCorrecto) {
	// Creamos un nuevo elemento toast
	let toast = document.createElement("div");
	toast.classList.add("toast");
	toast.setAttribute("role", "alert");
	toast.setAttribute("aria-live", "assertive");
	toast.setAttribute("aria-atomic", "true");
	toast.setAttribute("data-bs-delay", "5000");

	// Determinamos la clase de color basado en si esCorrecto es true o false
	let claseColor = esCorrecto ? "bg-success" : "bg-danger";
	let titulo = esCorrecto ? "Éxito" : "Error";

	// Creamos el contenido del toast
	toast.innerHTML = `
		<div class="toast-header ${claseColor}">
			<strong class="me-auto">${titulo}</strong>
			<button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
		</div>
		<div class="toast-body">
			${mensaje}
		</div>
	`;

	// Añadimos el toast al contenedor de toasts
	document.querySelector(".toast-container").appendChild(toast);

	// Mostramos el toast
	let bsToast = new bootstrap.Toast(toast);
	bsToast.show();
}