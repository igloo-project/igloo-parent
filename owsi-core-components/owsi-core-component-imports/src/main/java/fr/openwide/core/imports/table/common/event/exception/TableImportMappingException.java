package fr.openwide.core.imports.table.common.event.exception;

import fr.openwide.core.imports.table.common.location.TableImportLocation;


public class TableImportMappingException extends TableImportException {

	private static final long serialVersionUID = -2089979949013027692L;

	public TableImportMappingException(String message, TableImportLocation location) {
		super(message, location);
	}

	public TableImportMappingException(String message) {
		super(message);
	}

}
