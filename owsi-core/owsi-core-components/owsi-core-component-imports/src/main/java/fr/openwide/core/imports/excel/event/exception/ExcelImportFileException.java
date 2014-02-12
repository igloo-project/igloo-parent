package fr.openwide.core.imports.excel.event.exception;

import fr.openwide.core.imports.excel.location.ExcelImportLocation;

public class ExcelImportFileException extends ExcelImportException {

	private static final long serialVersionUID = 3491493477155038190L;

	public ExcelImportFileException(String message, Throwable cause, ExcelImportLocation location) {
		super(message, cause, location);
	}

	public ExcelImportFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExcelImportFileException(Throwable cause) {
		super(cause);
	}

	public ExcelImportFileException(Throwable cause, ExcelImportLocation location) {
		super(cause, location);
	}

}
