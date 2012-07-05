(function($) {
	var methods = {
		init : function(options) {
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
					
					$(options.inputSelector, $this).attr('disabled', !options.enabled);
					if (!options.enabled) {
						$this.addClass(data.options.disabledClass);
					}
					
					$(options.inputSelector, $this).on('keyup', function () {
						var filter = $.trim($(this).val().replace(/\s+/g, ' '));
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
										text = $.trim(text.replace(/\s+/g, ' '));
										if (text.search(filterPart, 'i') >= 0) {
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
				$(data.options.inputSelector, $this).val('');
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
				$(data.options.inputSelector, $this).attr('disabled', !enabled);
			});
		}
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