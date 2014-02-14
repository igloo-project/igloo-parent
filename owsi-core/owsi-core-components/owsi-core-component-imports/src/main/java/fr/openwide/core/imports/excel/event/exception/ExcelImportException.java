package fr.openwide.core.imports.excel.event.exception;

import fr.openwide.core.imports.excel.location.ExcelImportLocation;

public class ExcelImportException extends Exception {

	private static final long serialVersionUID = -4988670342820463252L;
	
	private final ExcelImportLocation location;

	public ExcelImportException(String message) {
		super(message);
		this.location = null;
	}

	public ExcelImportException(Throwable cause) {
		super(cause);
		this.location = null;
	}

	public ExcelImportException(String message, Throwable cause) {
		super(message, cause);
		this.location = null;
	}

	public ExcelImportException(String message, ExcelImportLocation location) {
		super(message);
		this.location = location;
	}

	public ExcelImportException(Throwable cause, ExcelImportLocation location) {
		super(cause);
		this.location = location;
	}

	public ExcelImportException(String message, Throwable cause, ExcelImportLocation location) {
		super(message, cause);
		this.location = location;
	}

	public ExcelImportLocation getLocation() {
		return location;
	}
}
