/**
 * Wrapper simple autour de la fenêtre modale pour gérer des fenêtres de confirmation.
 */
!function( $, window, document, undefined ) {
	
	"use strict";
	
	$.fn.confirm = function(options) {
		options = $.extend({}, $.fn.confirm.defaults, options);
		$(this).each(function(index) {
			var $this = $(this);
			var text = $this.data("modal-confirm-text");
			var title = $this.data("modal-confirm-title");
			var yesLabel = $this.data("modal-confirm-yes-label");
			var noLabel = $this.data("modal-confirm-no-label");
			var yesIcon = $this.data("modal-confirm-yes-icon");
			var noIcon = $this.data("modal-confirm-no-icon");
			var yesButton = $this.data("modal-confirm-yes-button");
			var noButton = $this.data("modal-confirm-no-button");
			var noEscape = $this.data("modal-confirm-text-noescape");
			var cssClassNames = $this.data("modal-confirm-css-class-names");
			var $modal = $("<div class='modal confirm'></div>");
			var $dialog = $("<div class='modal-dialog'></div>");
			var $content = $("<div class='modal-content'></div>");
			
			if (cssClassNames) {
				$content.addClass(cssClassNames);
			}
			
			$content.appendTo($('body'));
			
			var onConfirm = function(event) {
				$this.trigger('confirm');
				$content.modal('hide');
				event.preventDefault();
			};
			var onCancel = function(event) {
				$this.trigger('cancel');
				$content.modal('hide');
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
								$("<button class='" + noButton + "' data-dismiss='modal' type='button'></button>")
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
			$modal.modal({ show: true, backdrop: 'static' });
		});
	};
}(window.jQuery, window, document);