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
				datePickerCourant.datepicker('option', 'minDate', thisElement._getMinDate());
				datePickerCourant.datepicker('option', 'maxDate', thisElement._getMaxDate());
			});
			var actionOnUpdate = this.options.actionOnUpdate;
			if (actionOnUpdate && "NOTHING" != actionOnUpdate) {
				if (this.options.precedents) {
					$.each(this.options.precedents, function(index, value) {
						// on vérifie que le precedent est un élément de la page
						if (value && $(value)) {
							$(value).on('change', function(event) {
								// si la date n'est pas paramétrée, on ne touche à rien
								var currentDate = datePickerCourant.datepicker("getDate");
								var $this = $(this);
								if ($this.datepicker) {
									var precedentDate = $this.datepicker("getDate");
									if (precedentDate && currentDate && precedentDate > currentDate) {
										if ("UPDATE" == actionOnUpdate) {
											// on remplace la date
											datePickerCourant.val($this.val());
										} else {
											// on vide
											datePickerCourant.val("");
										}
									}
								}
							});
						}
					});
				}
				if (this.options.suivants) {
					$.each(this.options.suivants, function(index, value) {
						// on vérifie que le suivant est un élément de la page
						if (value && $(value)) {
							$(value).on('change', function(event) {
								// si la date n'est pas paramétrée, on ne touche à rien
								var currentDate = datePickerCourant.datepicker("getDate");
								var $this = $(this);
								if ($this.datepicker) {
									var suivantDate = $this.datepicker("getDate");
									if (suivantDate && currentDate && suivantDate < currentDate) {
										if ("UPDATE" == actionOnUpdate) {
											// on remplace la date
											datePickerCourant.val($this.val());
										} else {
											// on vide
											datePickerCourant.val("");
										}
									}
								}
							});
						}
					});
				}
			}
		},
		
		_getMinDate: function() {
			var minDate = this.options.precedentsModelsMaxDate;
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
			var maxDate = this.options.suivantsModelsMinDate;
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
			precedents:					[],
			precedentsModelsMaxDate:	null,
			suivants:	[],
			suivantsModelsMinDate:		null,
			reset:		false
	};
}(window.jQuery, window, document);