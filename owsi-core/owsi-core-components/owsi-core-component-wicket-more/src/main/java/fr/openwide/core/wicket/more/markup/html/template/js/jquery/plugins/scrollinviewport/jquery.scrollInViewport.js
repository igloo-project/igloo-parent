!function( $, window, document, undefined ) {
	
	"use strict";
	
	var ScrollInViewport = function (element, options) {
		this.init('scrollInViewport', element, options);
	};
	
	ScrollInViewport.prototype = {
		
		constructor: ScrollInViewport,
		
		init : function(type, element, options) {
			this.type = type;
			this.element = element;
			this.$element = $(element);
			this.options = this.getOptions(options);
			this.originalElementOffsetTop = this.$element.offset().top;
			$(window).on("scroll." + this.type, $.proxy(this._onScrollThrottle, this));
			$(window).trigger("scroll");
		},
		
		_onScrollThrottle : function() {
			// pour limiter le débit des événements, à chaque fois :
			// on vide la queue des événements
			// on lance avec un délai de 500ms l'événement de scroll
			// on relance la queue
			this.$element.stop(this.type, true).delay(this.options.delay, this.type).queue(this.type, $.proxy(this._onScroll, this)).dequeue(this.type);
		},
		
		getOptions: function (options) {
			options = $.extend({}, $.fn[this.type].defaults, options, this.$element.data())
			return options;
		},
		
		_onScroll : function(next) {
			var scrollTop = $(window).scrollTop();								// position du haut de l'écran dans la page
			var top = Math.max(0, scrollTop - this._getOffsetTop());
			if (top != 0) {
				top = top + this.options.margin;
			}
			this.$element.css({ position: "relative" });
			this.$element.animate({ top: top });	// animation jusqu'à la nouvelle position
			next();
		},
		
		_getOffsetTop : function() {
			var referenceComponent = this.options.referenceComponent;
			if (referenceComponent != null) {
				return referenceComponent.offset().top;
			} else {
				return this.originalElementOffsetTop;
			}
		}
	};
	
	$.fn.scrollInViewport = function (option) {
		return this.each(function () {
			var $this = $(this)
				, data = $this.data('scrollInViewport')
				, options = typeof option == 'object' && option;
			if (!data) {
				$this.data('scollInViewport', (data = new ScrollInViewport(this, options)));
			}
			if (typeof option == 'string') {
				data[option]();
			}
		})
	};
	
	$.fn.scrollInViewport.Constructor = ScrollInViewport;
	
	$.fn.scrollInViewport.defaults = {
		delay : 500, // délai avant prise en compte du scroll event ; permet de ne pas recaler la carte à chaque
		             // événement, mais seulement une fois que la fin du scroll est atteinte.
		margin: 10,  // marge utilisée par rapport au haut de l'écran dans le cas où l'élément est scrollé ; permet
		             // d'introduire un petit écart esthétique entre le haut de la page et l'élément.
		referenceComponent : null, // Composant de référence pour le scroll. Si null, on scroll par rapport à la position de départ.
	};
}(window.jQuery, window, document)