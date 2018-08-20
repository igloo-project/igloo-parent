(function($) {
	function findAllInputs($element) {
		return $element.find('input, select, textarea').andSelf().filter('input, select, textarea');
	}
	
	function resetAllInputs($element) {
		var $allInputs = findAllInputs($element);
		$allInputs.filter('input[type!="checkbox"][type!="radio"], textarea').val(null);
		$allInputs.prop('checked', false).prop('selected', false);
	}
	
	var methods = {
		init : function(options) {
			return this.each(function() {
				var $conditionalInputContainer = $(this);
				
				var data = $conditionalInputContainer.data('conditionalInput');
				if (data == null) {
					data = new Object();
					data.options = options;
					
					options.action = options.action || 'show';
					if (options.action == 'show') {
						options.action = function(checked) {
							var visible = checked;
							$(this).toggle(visible);
							if (!visible) {
								resetAllInputs($conditionalInputContainer);
							}
						};
					} else if (options.action == 'disable') {
						options.action = function(checked) {
							var disabled = checked;
							findAllInputs($(this)).prop('disabled', disabled);
							if (disabled) {
								resetAllInputs($conditionalInputContainer);
							}
						};
					} else if (options.action == 'enable') {
						options.action = function(checked) {
							var disabled = !checked;
							findAllInputs($(this)).prop('disabled', disabled);
							if (disabled) {
								resetAllInputs($conditionalInputContainer);
							}
						};
					}
					
					function applyAction(checked) {
						options.action.call($conditionalInputContainer, checked);
					}
					
					var enableOptionChecked = options.enableOption.attr('checked') !== undefined;
					applyAction(enableOptionChecked);
					
					var boxes = options.enableOption;
					var name = options.enableOption.prop('name');
					if (name !== undefined) {
						boxes = boxes.add(options.enableOption.closest('form, fieldset').find('input[name="' + name + '"]'));
					}
					boxes.on('change', function() {
						var checked = options.enableOption.prop('checked');
						applyAction(checked);
					});
				}
				
				$conditionalInputContainer.data('conditionalInput', data);
			});
		}
	};
	
	$.fn.conditionalInput = function(method) {
		// Method calling logic
		if (methods[method]) {
			return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist on jQuery.conditionalInput');
		}
	};
	
}) (jQuery);