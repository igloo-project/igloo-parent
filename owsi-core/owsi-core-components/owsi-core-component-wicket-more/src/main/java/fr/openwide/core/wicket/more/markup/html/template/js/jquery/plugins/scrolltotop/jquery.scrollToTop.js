(function($) {
	$.fn.scrollToTop = function() {
		var element = this;
		
		$(window).scroll(function() {
			// On ajoute une fonction quand on défile dans le site
			// On récupère la position de la barre de défilement par rapport à notre fenêtre
			var scrollTop = $(window).scrollTop();
			// On ajoute une fonction au clic de notre élément.
			
			if (scrollTop >= 450) { // Si on dépasse les 450 pixels
				if (element.is(':hidden')) { // et si la div est cachée
					element.show();
					element.stop().animate({opacity: 1}, 500); // On affiche l'élément
				}
			} else {
				// Sinon, on cache la div si celle ci est visible.
				if (!element.is(':hidden')) {
					element.stop().animate({opacity: 0}, 100);
					element.hide();
				}
			}
		});
		
		// On lance l'évènement scroll un première fois au chargement de la page
		$(window).scroll();
		
		element.click(function() {
			// On lance l'animation pour retourner en haut de la page
			$('html,body').stop().animate({scrollTop: 0}, 'normal');
		});
	};
	
	$.fn.scrollTo = function(item) {
		var $item = $(item).first();
		var itemTop = $item.offset().top;
		if (itemTop) {
			$('html,body').stop().animate({scrollTop: itemTop}, 'normal');
		}
	}
})(jQuery);