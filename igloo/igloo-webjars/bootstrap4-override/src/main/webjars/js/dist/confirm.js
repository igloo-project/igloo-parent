!function( $, window, document, undefined ) {
	
	"use strict";
	
	$.fn.confirm = function(options) {
		options = $.extend({}, $.fn.confirm.defaults, options);
		$(this).each(function(index) {
			var $this = $(this);
			var text = $this.data("bs-text");
			var title = $this.data("bs-title");
			var yesLabel = $this.data("bs-yes-label");
			var noLabel = $this.data("bs-no-label");
			var yesIcon = $this.data("bs-yes-icon");
			var noIcon = $this.data("bs-no-icon");
			var yesButton = $this.data("bs-yes-button");
			var noButton = $this.data("bs-no-button");
			var noEscape = $this.data("bs-no-escape");
			var cssClassNames = $this.data("bs-css-class-names");
			var $modal = $("<div class='modal fade confirm' tabindex='-1'></div>");
			var $dialog = $("<div class='modal-dialog'></div>");
			var $content = $("<div class='modal-content'></div>");
			
			$modal.on('hidden.bs.modal', function(e) {
				$modal.modal('dispose');
				$modal.remove();
			})
			
			if (cssClassNames) {
				$content.addClass(cssClassNames);
			}
			
			$content.appendTo($('body'));
			
			var onConfirm = function(event) {
				$this.trigger('confirm.bs.confirm');
				$modal.modal('hide');
				event.preventDefault();
			};
			var onCancel = function(event) {
				$this.trigger('cancel.bs.confirm');
				$modal.modal('hide');
				event.preventDefault();
			};
			
			$content
				.append(
						$("<div class='modal-header'></div>")
							.append($("<h5 class='modal-title'></h5>").text(title))
							.append("<button class='close' data-dismiss='modal' type='button'><span>&times;</span></button>")
				);
				
				if (noEscape) {
					$content.append($("<div class='modal-body'></div>").html(text))
				} else {
					$content.append($("<div class='modal-body'></div>").text(text))
				}
				
				$content.append(
						$("<div class='modal-footer'></div>")
							.append(
								$("<button class='" + noButton + "' type='button'></button>")
									.append($("<span class='" + noIcon +"'></span>"))
									.append(document.createTextNode(" " + noLabel))
									.click(onCancel)
							)
							.append(
								$("<button class='" + yesButton + "' type='button'></button>")
									.append($("<span class='" + yesIcon + "'></span>"))
									.append(document.createTextNode(" " + yesLabel))
									.click(onConfirm)
							)
				);
			
			$dialog.append($content);
			$modal.append($dialog);
			$modal.modal({ show: true });
		});
	};
}(window.jQuery, window, document);