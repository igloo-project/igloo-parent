

!function( $ ) {
	"use strict"
	
	$.fn.alert = {
		show: function(selector, autohideDelay) {
			var $selector = $(selector);
			var placement = $selector.data("placement");
			var $panel = $(".alert-global-panel", $selector);
			$panel.off("click.alert");
			if ($(".alert-info, .alert-warning, .alert-error, .alert-success" , $panel).size() > 0) {
				if (placement == "bottom") {
					$panel.animate({ bottom: 0 }, 500);
				} else {
					$panel.animate({ top: 0 }, 500);
				}
				
				if ($(".alert-info, .alert-warning, .alert-error", $panel).size() == 0) {
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
					}
				}
			}
		},
		hide: function(selector) {
			var $selector = $(selector);
			var placement = $selector.data("placement");
			var $panel = $(".alert-global-panel", $selector);
			if ($(".alert-info, .alert-warning, .alert-error, .alert-success" , $panel).size() > 0) {
				if (placement == "bottom") {
					$panel.css({ bottom: -$panel.outerHeight() });
				} else {
					$panel.css({ top: -$panel.outerHeight() });
				}
			}
		},
		reset: function(selector, autohideDelay) {
			$.fn.alert.hide(selector);
			$(".alert-global-panel", $(selector)).show();
			$.fn.alert.show(selector, autohideDelay);
		},
		close: function(event) {
			var $panel = $(event.target).closest(".alert-global-panel");
			var placement = $(event.target).closest(".animated-global-feedback").data("placement");
			if ($(".alert-info, .alert-warning, .alert-error, .alert-success" , $panel).size() > 0) {
				if (placement == "bottom") {
					$panel.stop(true).animate({ bottom: -$panel.outerHeight() });
				} else {
					$panel.stop(true).animate({ top: -$panel.outerHeight() });
				}
			}
			event.preventDefault();
		}
	};
}( window.jQuery )