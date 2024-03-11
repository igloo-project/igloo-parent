'use strict'

export function init(id) {
	const el = document.getElementById(id);
	showPasswordElement(el, false);
	el.addEventListener('click', () => {
		showPasswordElement(el);
	});
}

export function showPasswordElement(el, toggle = true) {
	const target = document.getElementById(el.dataset.showPasswordTarget);
	
	if (!target) {
		return;
	}
	
	if (toggle) {
		el.classList.toggle('active');
	}
	
	const active = el.classList.contains('active');
	
	el.setAttribute('aria-pressed', active ? 'true' : 'false')
	target.type = active ? 'text' : 'password';
}
