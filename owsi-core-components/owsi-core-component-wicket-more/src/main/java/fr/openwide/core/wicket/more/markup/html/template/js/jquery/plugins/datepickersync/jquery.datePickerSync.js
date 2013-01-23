!function( $, window, document, undefined ) {
	
	"use strict";
	
	var DatePickerSync = function (element, options) {
		this.init('datePickerSync', element, options);
	};
	
	DatePickerSync.prototype = {
		
		constructor: DatePickerSync,
		
		init : function(type, element, options) {
			this.type = type;
			this.$element = $(element);
			this.options = this.getOptions(options);
			
			var thisElement = this;
			
			var datePickerCourant = this.$element;
			
			datePickerCourant.datepicker('option', 'beforeShow', function(input, inst) {
				if (thisElement.options.precedents.length > 0) {
					datePickerCourant.datepicker('option', 'minDate', thisElement._getMinDate());
				}
				if (thisElement.options.suivants.length > 0) {
					datePickerCourant.datepicker('option', 'maxDate', thisElement._getMaxDate());
				}
			});
		},
		
		_getMinDate: function() {
			var minDate = null;
			var precedents = this.options.precedents;
			var nbPrecedents = precedents.length;
			if (nbPrecedents > 0) {
				for (var i=0; i<nbPrecedents; i++) {
					var date = precedents[i].datepicker('getDate');
					if (date != null) {
						if (minDate == null || date.getTime() > minDate.getTime()) {
							minDate = date;
						}
					}
				}
			}
			return minDate;
		},
		
		_getMaxDate: function() {
			var maxDate = null;
			var suivants = this.options.suivants;
			var nbSuivants = suivants.length;
			if (nbSuivants > 0) {
				for (var i=0; i<nbSuivants; i++) {
					var date = suivants[i].datepicker('getDate');
					if (date != null) {
						if (maxDate == null || date.getTime() < maxDate.getTime()) {
							maxDate = date;
						}
					}
				}
			}
			return maxDate;
		},
		
		getOptions: function (options) {
			options = $.extend({}, $.fn[this.type].defaults, options, this.$element.data());
			return options;
		},
		
	};
	
	$.fn.datePickerSync = function (option) {
		return this.each(function () {
			var $this = $(this)
				, data = $this.data('datePickerSync')
				, options = typeof option == 'object' && option;
			if (!data) {
				$this.data('datePickerSync', (data = new DatePickerSync(this, options)));
			}
			if (typeof option == 'string') {
				data[option]();
			}
		});
	};
	
	$.fn.datePickerSync.Constructor = DatePickerSync;
	
	$.fn.datePickerSync.defaults = {
			precedents:	[],
			suivants:	[],
			reset:		false
	};
}(window.jQuery, window, document);