(function($) {
	// http://support.microsoft.com/kb/167820
	function getIeVersion() {
		var ua = window.navigator.userAgent;
		var msie = ua.indexOf("MSIE ");
		if (msie > 0) // If Internet Explorer, return version number
			return parseInt(ua.substring(msie + 5, ua.indexOf(".", msie)));
		else
			// If another browser, return nothing
			return undefined;
	}
	var ieVersion = getIeVersion();
	var bindOnResize;
	if (ieVersion && ieVersion < 9) {
		// IE<9 supports binding a handler for the resize event on any element, and *requires* this
		// when using respond.js to emulate media queries (respond.js resizes elements just after rendering
		// the page, but does not fire a resize event on the window).
		bindOnResize = function($plot, replot) {
			$plot.on('resize', replot);
		};
	} else {
		bindOnResize = function($plot, replot) {
			$(window).on('resize', replot);
		};
	}
	function postInit(target, data, options) {
		var plot = this;
		function replot() {
			plot.replot( { resetAxes: true } );
		}
		bindOnResize(plot.target, replot);
	}
	$.jqplot.postInitHooks.push(postInit);
})(jQuery);