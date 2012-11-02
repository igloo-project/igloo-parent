(function($) {
	var methods = {
		init : function(options) {
			var cleanInput = function(input) {
				if (!input) { return ''; }
				var accentMap = {
					'à':'a','á':'a','â':'a','é':'e','è':'e','ê':'e','ë':'e','í':'i','ï':'i','ô':'o','ó':'o','ú':'u','û':'u','ù':'u','ç':'c'
				};
				var s = input.toLowerCase();
				var ret = '';
				for (var i = 0; i < s.length; i++) {
					ret += accentMap[s.charAt(i)] || s.charAt(i);
				}
				return ret;
			};
			
			return this.each(function() {
				var $this = $(this);
				
				var data = $this.data('listFilter');
				if (data == null) {
					data = new Object();
					data.options = options;
					
					if (!options.containerClass) {
						options.containerClass = 'filter-list-container';
					}
					if (!options.inputSelector) {
						options.inputSelector = '.filter-input';
					}
					if (!options.itemsSelector) {
						options.itemsSelector = '.filter-element';
					}
					if (!options.hiddenElementClass) {
						options.hiddenElementClass = 'filter-hidden-element';
					}
					if (!options.emptyListClass) {
						options.emptyListClass = 'filter-empty-list';
					}
					if (options.enabled == null) {
						options.enabled = true;
					}
					if (!options.disabledClass) {
						options.disabledClass = 'disabled';
					}
					
					$this.addClass(options.containerClass);
					
					var inputField = $(options.inputSelector, $this);
					if (inputField.length == 0) {
						inputField = $(options.inputSelector);
					}
					
					inputField.attr('disabled', !options.enabled);
					if (!options.enabled) {
						$this.addClass(data.options.disabledClass);
					}
					
					inputField.on('keyup', function () {
						var filter = cleanInput($.trim($(this).val().replace(/\s+/g, ' ')));
						var $listeContainer = $this;
						
						if (filter.length == 0) {
							$listeContainer.removeClass(options.emptyListClass);
							$(options.itemsSelector, $this).each(function () {
								$(this).removeClass(options.hiddenElementClass);
							});
						} else {
							var nbElt = 0;
							var filterParts = filter.split(' ');
							
							$(options.itemsSelector, $this).each(function () {
								var $elementsToScan;
								if (options.scanSelector) {
									$elementsToScan = $(options.scanSelector, this);
								} else {
									$elementsToScan = $(this);
								}
								
								var hide = false;
								
								for (i in filterParts) {
									var filterPart = filterParts[i];
									var filterPartFound = false;
									
									$elementsToScan.each(function() {
										var text = $(this).text();
										text = cleanInput($.trim(text.replace(/\s+/g, ' ')));
										if (text.indexOf(filterPart) >= 0) {
											filterPartFound = true;
										}
									});
									
									if (!filterPartFound) {
										hide = true;
									}
								}
								
								if (hide) {
									$(this).addClass(options.hiddenElementClass);
								} else {
									$(this).removeClass(options.hiddenElementClass);
									nbElt++;
								}
							});
							
							if (nbElt == 0) {
								$listeContainer.addClass(options.emptyListClass);
							} else {
								$listeContainer.removeClass(options.emptyListClass);
							}
						}
					}); 
				}
				
				$this.data('listFilter', data);
			});
		},
		clear : function() {
			return this.each(function() {
				var $this = $(this), data = $this.data('listFilter');
				
				var inputField = $(data.options.inputSelector, $this);
				if (inputField.length == 0) {
					inputField = $(data.options.inputSelector);
				}
				
				inputField.val('');
				$(data.options.inputSelector, $this).trigger('keyup');
			});
		},
		setEnabled : function(enabled) {
			return this.each(function() {
				var $this = $(this), data = $this.data('listFilter');
				if (enabled) {
					$this.removeClass(data.options.disabledClass);
				} else {
					$this.addClass(data.options.disabledClass);
				}
				var inputField = $(data.options.inputSelector, $this);
				if (inputField.length == 0) {
					inputField = $(data.options.inputSelector);
				}
				inputField.attr('disabled', !enabled);
			});
		},
	};
	
	$.fn.listFilter = function(method) {
		// Method calling logic
		if (methods[method]) {
			return methods[method].apply(this, Array.prototype.slice.call(
					arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist on jQuery.listFilter');
		}
	};
	
}) (jQuery);