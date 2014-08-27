!function( $, window, document, undefined ) {
	
	"use strict";
	
	var nonce = 0;
	var globalVariableName = function() { return $.expando + "_fileUpload_" + nonce++; };
	
	if (!window.console) {
		var console = {
			log : function() {
			}
		};
	} else {
		var console = window.console;
	}
	
	if (!$.fileuploadglue) {
		$.fileuploadglue = {};
	};
	
	$.fileuploadglue.onChange = function (event, data) {
		var $that = $(this);
		var onChangeCallback = $(this).fileupload('option', 'onChangeCallback');
		var fileList = [];
		$.each(data.files, function(index, file) {
			var fileItem = {};
			fileItem.name = file.name;
			fileItem.size = file.size;
			fileItem.type = file.type;
			fileItem.objectUrl = window.URL.createObjectURL(file);
			fileList.push(fileItem);
		});
		var dataSend = {};
		var dataVariableName = globalVariableName();
		window[dataVariableName] = null;
		dataSend.files = data.files;
		
		var success = function(data, status) {
			dataSend.formData = {};
			dataSend.formData["fileList"] = JSON.stringify(window[dataVariableName]);
			$that.fileupload('send', dataSend);
		};
		var error = function() { console.log(arguments) };
		onChangeCallback(dataVariableName, JSON.stringify(fileList), success, error);
	};
	
	/**
	 * Génère une callback de progressall qui cible le composant avec l'id indiqué en paramètre.
	 */
	$.fileuploadglue.progressallCallbackGenerator = function(id) {
		return function (e, data) {
			var progress = parseInt(data.loaded / data.total * 100, 10);
			$('#' + id + ' .bar').css(
				'width',
				progress + '%'
			);
		};
	};
}(window.jQuery, window, document)