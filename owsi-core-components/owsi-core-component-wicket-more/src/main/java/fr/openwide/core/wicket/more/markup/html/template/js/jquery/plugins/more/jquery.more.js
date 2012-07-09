(function($) {
	var methods = {
		init : function(options) {
			options = $.extend({}, $.fn.more.defaults.options, options);
			return this.each(function() {
				var $this = $(this);
				$this.wrapInner("<div />");
				$this.addClass("more");
				
				var more = $this.data('more');
				if (more == null) {
					var containerHeight = $this.height();
					var containedHeight = $("div", $this).outerHeight();
					
					if (containedHeight > containerHeight) {
						$this.data("more", {visible: false});
						var linkContainer = $("<div></div>").addClass("more-label");
						var label = $this.data("more-label") || options.label;
						var link = $("<a></a>")
							.attr("href", "#")
							.text(label + " »")
							.appendTo(linkContainer)
							.click(function (event) {
								var data = $this.data("more");
								if (!data.visible) {
									$this.css({ maxHeight: "none", height: containerHeight });
									$this.animate({ height: containedHeight }, "slow");
									linkContainer.find("a").text("^");
									linkContainer.addClass("visible");
									event.preventDefault();
									$this.data("more", {visible: true, maxHeight: containerHeight});
								} else {
									$this.animate({ height: data.maxHeight }, "slow");
									linkContainer.find("a").text(label + " »");
									linkContainer.removeClass("visible");
									event.preventDefault();
									$this.data("more", {visible: false});
								}
								event.target.blur();
						});
						
						linkContainer.appendTo($this);
					}
					
				}
			});
		}
	};

	$.fn.more = function(method) {
		// Method calling logic
		if (methods[method]) {
			return methods[method].apply(this, Array.prototype.slice.call(
					arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return methods.init.apply(this, arguments);
		} else {
			$.error('Method ' + method + ' does not exist on jQuery.more');
		}
	};

	$.fn.more.defaults = {
		label: "", // utilisé si pas d'attribut data-more-label
	};

}) (jQuery);