package fr.openwide.core.imports.excel.event.exception;

import fr.openwide.core.imports.excel.location.ExcelImportLocation;

public class ExcelImportHeaderLabelMappingException extends ExcelImportMappingException {

	private static final long serialVersionUID = -9130255186999010404L;
	
	private final String expectedHeaderLabel;

	public ExcelImportHeaderLabelMappingException(String message, String expectedHeaderLabel, ExcelImportLocation location) {
		super(message, location);
		this.expectedHeaderLabel = expectedHeaderLabel;
	}

	public ExcelImportHeaderLabelMappingException(String message, String expectedHeaderLabel) {
		super(message);
		this.expectedHeaderLabel = expectedHeaderLabel;
	}
	
	public String getExpectedHeaderLabel() {
		return expectedHeaderLabel;
	}

}
