

!function( $ ) {
	"use strict"
	
	$.fn.alert = {
		show: function(selector, autohideDelay) {
			var $selector = $(selector);
			var placement = $selector.data("placement");
			var $panel = $(".alert-global-panel, .feedback-container", $selector);
			$panel.off("click.alert");
			if ($(".alert", $panel).length > 0) {
				$panel.show();
				if (placement == "bottom") {
					$panel.animate({ bottom: 0 }, 500);
				} else {
					$panel.animate({ top: 0 }, 500);
				}
				
				if ($(".alert", $panel).not(".alert-success").length == 0) {
					$panel.on("click.alert", function(event) { $.fn.alert.close(event); });
					if (autohideDelay == null) {
						autohideDelay = 5000;
					}
					if (autohideDelay >= 0) {
						if (placement == "bottom") {
							$panel.delay(autohideDelay).animate({ bottom: -$panel.outerHeight() });
						} else {
							$panel.delay(autohideDelay).animate({ top: -$panel.outerHeight() });
						}
						$panel.hide(0);
					}
				}
			}
		},
		hide: function(selector) {
			var $selector = $(selector);
			var placement = $selector.data("placement");
			var $panel = $(".alert-global-panel, .feedback-container", $selector);
			if ($(".alert" , $panel).length > 0) {
				if (placement == "bottom") {
					$panel.css({ bottom: -$panel.outerHeight() });
				} else {
					$panel.css({ top: -$panel.outerHeight() });
				}
				$panel.hide(0);
			}
		},
		reset: function(selector, autohideDelay) {
			$.fn.alert.hide(selector);
			$.fn.alert.show(selector, autohideDelay);
		},
		close: function(event) {
			var $panel = $(event.target).closest(".alert-global-panel, .feedback-container");
			var placement = $(event.target).closest(".animated-global-feedback").data("placement");
			if ($(".alert" , $panel).length > 0) {
				if (placement == "bottom") {
					$panel.stop(true).animate({ bottom: -$panel.outerHeight() });
				} else {
					$panel.stop(true).animate({ top: -$panel.outerHeight() });
				}
				$panel.hide(0);
			}
			event.preventDefault();
		}
	};
}( window.jQuery )