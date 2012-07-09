;!function($, window, document, undefined) {
	
	"use strict";
	
	if (!window.console) {
		var console = { log: function() {} };
	} else {
		var console = window.console;
	}
	
	$.fn.gmapProxy = function () {
		return this.each(function () {
			var $this = $(this);
			if ($this.data("gmap").drawingManager) {
				$this.gmap.apply($(this), arguments);
			} else {
				$this.on("gmap.drawingManagerInitialized", function() { $this.gmap.apply(arguments); });
			}
		});
	};
}(window.jQuery, window, document)