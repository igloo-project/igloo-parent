;
var Igloo = Igloo || {};
Igloo.PlaceholderUtils = (function() {
	var settings = {
			customClass: 'placeholder'
	};
	var disable = function() {
		// Copy/paste from function clearPlaceholder in jquery.placeholder.js, with calls to select()/focus() commented out
		var input = this;
		var $input = $(input);
		
		if (input.value === $input.attr('placeholder') && $input.hasClass(settings.customClass)) {
			input.value = '';
			$input.removeClass(settings.customClass);
			if ($input.data('placeholder-password')) {
				$input = $input.hide().nextAll('input[type="password"]:first').show().attr('id', $input.removeAttr('id').data('placeholder-id'));
				// If `clearPlaceholder` was called from `$.valHooks.input.set`
//				if (event === true) {
					$input[0].value = value;
					return value;
//				}
//				$input.focus();
			} else {
//				input == safeActiveElement() && input.select();
			}
		}
	};

	return {
		disable: disable
	}
}) ();