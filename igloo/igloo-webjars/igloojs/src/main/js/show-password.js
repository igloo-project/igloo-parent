'use strict'

export function init(id) {
	let el = document.getElementById(id);
	showPasswordElement(el, false);
	el.addEventListener('click', () => {
		showPasswordElement(el);
	});
}

export function showPasswordElement(el, toggle = true) {
	let target = document.getElementById(el.dataset.showPasswordTarget);
	
	if (!target) {
		return;
	}
	
	if (toggle) {
		el.classList.toggle('active');
	}
	
	let active = el.classList.contains('active');
	
	el.setAttribute('aria-pressed', active ? 'true' : 'false')
	target.type = active ? 'text' : 'password';
}
