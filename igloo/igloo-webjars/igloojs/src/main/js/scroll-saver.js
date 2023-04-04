'use strict'

export function init(el) {
	restore(el);
	el.addEventListener('scroll', _.throttle(() => save(el), 100));
}

export function save(el) {
	let scrollSaverId = el.dataset.scrollSaverId;
	
	if (!scrollSaverId) {
		return;
	}
	
	sessionStorage.setItem('scroll-saver-' + scrollSaverId + '-top', el.scrollTop);
	sessionStorage.setItem('scroll-saver-' + scrollSaverId + '-left', el.scrollLeft);
}

export function restore(el) {
	let scrollSaverId = el.dataset.scrollSaverId;
	
	if (!scrollSaverId) {
		return;
	}
	
	let scrollSaverScrollBehavior = el.dataset.scrollSaverScrollBehavior;
	
	let top = sessionStorage.getItem('scroll-saver-' + scrollSaverId + '-top');
	let left = sessionStorage.getItem('scroll-saver-' + scrollSaverId + '-left');
	
	el.scrollTo({
		top: parseInt(top, 10),
		left: parseInt(left, 10),
		behavior: scrollSaverScrollBehavior ?  scrollSaverScrollBehavior : 'instant'
	});
}
