(function($) {
	var methods = {
		init : function(options) {
			options = $.extend({}, $.fn.more.defaults, options);
			return this.each(function() {
				var $this = $(this);
				$this.wrapInner('<div class="contained" />');
				$this.addClass("more");
				
				var more = $this.data('more');
				if (more == null) {
					var containerHeight = $this.height();
					var containedHeight = $('.contained', $this).outerHeight();
					
					if (containedHeight > containerHeight) {
						$this.data("more", {visible: false});
						
						var linkContainer = $('<div class="more-label">');
						var link = options.createMoreLink()
							.appendTo(linkContainer)
							.click(function (event) {
								var data = $this.data("more");
								if (!data.visible) {
									var actualHeight = $this.height();
									// Spécifier height: actualHeight permet de dérouler ensuite avec animate
									$this.css({ maxHeight: "none", height: actualHeight});
									$this.animate({ height: $('.contained', $this).outerHeight() + linkContainer.height() }, "slow");
									event.preventDefault();
									$this.data("more", {visible: true, maxHeight: actualHeight});
									
									$this.addClass("open");
								} else {
									$this.animate({ height: data.maxHeight }, "slow");
									event.preventDefault();
									$this.data("more", {visible: false});
									
									$this.removeClass("open");
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
		createMoreLink: function() {
			return link = $("<a></a>")
				.attr("href", "#")
				.append($("<span class='icon-plus more-icon-open'></span>"))
				.append($("<span class='icon-minus more-icon-close'></span>"));
		}
	};

}) (jQuery);