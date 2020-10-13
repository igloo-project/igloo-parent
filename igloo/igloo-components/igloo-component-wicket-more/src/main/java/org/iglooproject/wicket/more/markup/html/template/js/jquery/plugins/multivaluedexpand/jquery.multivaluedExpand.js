!function( $, window, document, undefined ) {
	
	"use strict";
	
	var MultivaluedExpand = function (element, options) {
		this.init('multivaluedExpand', element, options);
	};
	
	MultivaluedExpand.prototype = {
		
		constructor: MultivaluedExpand,
		
		init : function(type, element, options) {
			this.type = type;
			this.$element = $(element);
			this.options = this.getOptions(options);
			
			var elementContainer = this.$element;
			var multivaluedContainer = elementContainer.hasClass('multivalued') ? elementContainer : elementContainer.closest(".multivalued");
			var items = $(".multivalued-item", multivaluedContainer);
			
			if (items.length > 1) {
				var toggleButton = $(this.options.toggleButtonHtml).addClass('expand-toggle');
				toggleButton.on('click', function() {
					multivaluedContainer.toggleClass('closed');
				});
				
				elementContainer.prepend(toggleButton);
				multivaluedContainer.addClass('closed');
			}
		},
		
		getOptions: function (options) {
			options = $.extend({}, $.fn[this.type].defaults, options, this.$element.data());
			return options;
		},
		
	};
	
	$.fn.multivaluedExpand = function (option) {
		return this.each(function () {
			var $this = $(this)
				, data = $this.data('multivaluedExpand')
				, options = typeof option == 'object' && option;
			if (!data) {
				$this.data('multivaluedExpand', (data = new MultivaluedExpand(this, options)));
			}
			if (typeof option == 'string') {
				data[option]();
			}
		});
	};
	
	$.fn.multivaluedExpand.Constructor = MultivaluedExpand;
	
	$.fn.multivaluedExpand.defaults = {
			toggleButtonHtml : '<span><span class="fa fa-fw fa-plus-circle"></span><span class="fa fa-fw fa-minus-circle"></span></span>'
	};
}(window.jQuery, window, document);