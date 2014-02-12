package fr.openwide.core.imports.excel.event.exception;

import fr.openwide.core.imports.excel.location.ExcelImportLocation;

public class ExcelImportMappingException extends ExcelImportException {

	private static final long serialVersionUID = -2089979949013027692L;

	public ExcelImportMappingException(String message, ExcelImportLocation location) {
		super(message, location);
	}

	public ExcelImportMappingException(String message) {
		super(message);
	}

}
