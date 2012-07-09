;!function($, window, document, undefined) {
	
	"use strict";
	
	var BlockFocus = function (element, options) {
		this.init('blockFocus', element, options);
	};
	
	if (!window.console) {
		var console = { log: function() {} };
	} else {
		var console = window.console;
	}
	
	BlockFocus.prototype = {
		
		constructor: BlockFocus,
		
		init : function(type, element, options) {
			this.type = type;
			this.element = element;
			this.$element = $(element);
			this.options = this.getOptions(options);
			this.currentBlock = null;
			$(window).on("scroll." + this.type, $.proxy(this._onScrollThrottle, this));
			$(window).trigger("scroll");
		},
		
		getOptions: function (options) {
			options = $.extend({}, $.fn[this.type].defaults, options, this.$element.data())
			return options;
		},
		
		_onScrollThrottle : function() {
			// pour limiter le débit des événements, à chaque fois :
			// on vide la queue des événements
			// on lance avec un délai de 500ms l'événement de scroll
			// on relance la queue
			this.$element.stop(this.type, true).delay(this.options.delay, this.type).queue(this.type, $.proxy(this._onScroll, this)).dequeue(this.type);
		},
		
		_isVisibleOnScreen : function(jQueryElement, reduceViewport) {
			var viewportHeight = $(window).height();
			
			var scrollTop = $(window).scrollTop();
			var scrollBottom = scrollTop + viewportHeight;
			
			// retrécit l'écran de manière à favoriser le passage à la page suivante quand un
			// bloc arrive en bord de page.
			if (reduceViewport) {
				scrollTop = scrollTop + viewportHeight * this.options.viewportTopPercentage;
				scrollBottom = scrollBottom - viewportHeight * this.options.viewportBottomPercentage;
			}
			
			var jQueryElementTopOffset = jQueryElement.offset().top;
			var jQueryElementBottomOffset = jQueryElementTopOffset + jQueryElement.height();
			return (jQueryElementTopOffset > scrollTop && jQueryElementTopOffset < scrollBottom) // coin supérieur dans l'écran
					|| (jQueryElementBottomOffset > scrollTop && jQueryElementBottomOffset < scrollBottom) // coin inférieur dans l'écran
					|| (jQueryElementTopOffset < scrollTop && jQueryElementBottomOffset > scrollBottom); // coin supérieur au-dessus et coin inférieur en-dessous
		},
		
		_onScroll : function(next) {
			var scrollTop = $(window).scrollTop();
			var documentHeight = $(document).height();
			var scrollBottom = scrollTop + $(window).height();
			var focus = null;
			
			console.log(scrollBottom);
			console.log(documentHeight);
			if (scrollBottom > documentHeight - 20) {
				// cas particulier : on a atteind le bas de la page
				focus = $("> .block", this.$element).last();
			} else if (this.currentBlock == null || !this._isVisibleOnScreen(this.currentBlock, true)) {
				// lors de la première itération, le bloc courant peut être null
				// ou alors le bloc est non nul, mais il n'est plus visible
				
				// si le bloc courant est null, on commence à partir du début de la liste
				if (this.currentBlock == null) {
					focus = $("> .block:first", this.$element);
				} else {
					if (this.currentBlock.offset().top < scrollTop) {
						// cas où on est plus bas que l'ancien élément avec le focus
						focus = this.currentBlock.next(".block");
						while (focus.size() != 0 && !this._isVisibleOnScreen(focus)) {
							console.log("Recherche de bloc focus - next");
							focus = focus.next();
						}
					} else {
						// cas où on est plus haut que l'ancien élément avec le focus
						focus = this.currentBlock.prev(".block");
						while(focus.length != 0 && !this._isVisibleOnScreen(focus, true)) {
							console.log("Recherche de bloc focus - previous");
							focus = focus.prev();
						}
					}
				}
			}
			
			if (focus != null && focus.size() != 0) {
				console.log("Focus sur l'élément : ", focus);
				if (this.currentBlock) {
					this.currentBlock.removeClass("focus");
					this.currentBlock.trigger("blockUnfocus");
				}
				this.currentBlock = focus;
				this.currentBlock.trigger("blockFocus");
				this.currentBlock.addClass("focus");
			} else {
				console.log("Aucun bloc trouvé pour mise en place du focus. Focus conservé sur l'ancien bloc.")
			}
			
			next();
		},
	};
	
	$.fn.blockFocus = function (option) {
		return this.each(function () {
			var $this = $(this)
				, data = $this.data('blockFocus')
				, options = typeof option == 'object' && option;
			if (!data) {
				$this.data('blockFocus', (data = new BlockFocus(this, options)));
			}
			if (typeof option == 'string') {
				data[option]();
			}
		})
	};
	
	$.fn.blockFocus.Constructor = BlockFocus;
	
	$.fn.blockFocus.defaults = {
		delay: 500,
		viewportTopPercentage: .45,
		viewportBottomPercentage: .45,
	};
}(window.jQuery, window, document)