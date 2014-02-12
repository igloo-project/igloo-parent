package fr.openwide.core.imports.excel.event.exception;

import fr.openwide.core.imports.excel.location.ExcelImportLocation;


public class ExcelImportContentException extends ExcelImportException {

	private static final long serialVersionUID = 2545352534086805755L;

	public ExcelImportContentException(String message, ExcelImportLocation location) {
		super(message, location);
	}

	public ExcelImportContentException(String message) {
		super(message);
	}

}
