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
			var noEscape = $this.data("modal-confirm-text-noescape");
			var $content = $("<div class='modal confirm'></div>");
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
						$("<div class='modal-header'></div>").
							append("<a class='close' data-dismiss='modal'>&#x00d7;</a>")
							.append($("<h3></h3>").text(title))
				);
				
				if (noEscape) {
					$content.append($("<div class='modal-body'></div>").html(text))
				} else {
					$content.append($("<div class='modal-body'></div>").text(text))
				}
				
				$content.append(
						$("<div class='modal-footer'></div>")
							.append(
								$("<button class='btn' href='#' data-dismiss='modal'></a>")
									.append($("<span class='icon-ban-circle'></span>"))
									.append(document.createTextNode(" " + noLabel))
									.click(onCancel)
							)
							.append(
								$("<button class='btn btn-success' href='#'></a>")
									.append($("<span class='icon-ok icon-white'></span>"))
									.append(document.createTextNode(" " + yesLabel))
									.click(onConfirm)
							)
				);
			$content.modal('show');
		});
	};
}(window.jQuery, window, document);