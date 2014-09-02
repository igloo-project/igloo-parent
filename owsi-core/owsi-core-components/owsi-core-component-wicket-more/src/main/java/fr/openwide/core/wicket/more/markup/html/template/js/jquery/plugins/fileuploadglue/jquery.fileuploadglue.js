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
	
	$.fileuploadglue.onUploadFails = function(data, textState, jqXHR) {
		console.log('uploadFails event', data);
		var onUploadFailsCallback = $(this).fileupload('option', 'onUploadFailsCallback');
		var dataVariableName = globalVariableName();
		onUploadFailsCallback(dataVariableName, data.uploadFailsErrorMessage,
				function() { console.log('uploadFails ajax send success'); },
				function(event, data) { console.log('uploadFails ajax send error', data) }
		);
	};
	$.fileuploadglue.onUploadDone = function(data, textState, jqXHR) {
		console.log('uploadDone event', data);
		if (data.uploadFailsErrorMessage) {
			$.proxy($.fileuploadglue.onUploadFails, this)(data, textState, jqXHR);
		} else {
			var onUploadDoneCallback = $(this).fileupload('option', 'onUploadDoneCallback');
			var dataVariableName = globalVariableName();
			onUploadDoneCallback(dataVariableName, JSON.stringify(data.successFileList),
					JSON.stringify(data.errorFileList),
					function() { console.log('uploadDone ajax send success'); },
					function(event, data) { console.log('uploadDone ajax send error', data) });
		}
	};
	$.fileuploadglue.onChange = function (event, data) {
		var that = this;
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
			file.glueObjectUrl = fileItem.objectUrl;
		});
		var dataSend = {};
		var dataVariableName = globalVariableName();
		window[dataVariableName] = null;
		var originalFiles = data.files;
		
		var success = function(data, status) {
			dataSend.formData = {};
			var toSendFiles = [];
			// only files referenced in fileList are uploaded
			$.each(originalFiles, function(index, fileApiItem) {
				$.each(window[dataVariableName], function(index, acceptedFile) {
					if (fileApiItem.glueObjectUrl == acceptedFile.objectUrl) {
						toSendFiles.push(fileApiItem);
						console.log('Upload file :', fileApiItem.name);
						return;
					}
				});
			});
			dataSend.formData["fileList"] = JSON.stringify(window[dataVariableName]);
			dataSend.files = toSendFiles;
			$that.fileupload('send', dataSend).success($.proxy($.fileuploadglue.onUploadDone, that)).error($.proxy($.fileuploadglue.onUploadFails, that));
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