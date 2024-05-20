function toggleDescription(button) {
	var cardBody = button.closest('.card-body');
	var shortDesc = cardBody.querySelector('.short-desc');
	var fullDesc = cardBody.querySelector('.full-desc');
	var icon = button.querySelector('#enlaceVerMas');

	if (shortDesc.style.display === 'none') {
		shortDesc.style.display = 'inline';
		fullDesc.style.display = 'none';
		icon.classList.remove('fa-caret-up');
		icon.classList.add('fa-caret-down');
		button.innerHTML = '<i class="fa-solid fa-caret-down" id="enlaceVerMas"></i> Ver más';
	} else {
		shortDesc.style.display = 'none';
		fullDesc.style.display = 'inline';
		icon.classList.remove('fa-caret-down');
		icon.classList.add('fa-caret-up');
		button.innerHTML = '<i class="fa-solid fa-caret-up" id="enlaceVerMas"></i> Ver menos';
	}
}