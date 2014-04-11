package fr.openwide.core.imports.excel.event;

import fr.openwide.core.imports.excel.event.exception.ExcelImportContentException;
import fr.openwide.core.imports.excel.event.exception.ExcelImportHeaderLabelMappingException;
import fr.openwide.core.imports.excel.event.exception.ExcelImportMappingException;
import fr.openwide.core.imports.excel.location.ExcelImportLocation;

/**
 * A simple event handler that will throw an exception on every single error.
 * <p>No special caring is necessary when using this event handler; you may skip calling {@link #checkNoErrorOccurred()}.
 */
public class SimpleExcelImportEventHandler implements IExcelImportEventHandler {

	@Override
	public void headerLabelMappingError(String expectedHeaderLabel, int indexAmongMatchedColumns, ExcelImportLocation location) throws ExcelImportHeaderLabelMappingException {
		throw new ExcelImportHeaderLabelMappingException(
				"Could not map column '" + expectedHeaderLabel + "' (index among matched columns : " + indexAmongMatchedColumns + ")",
				expectedHeaderLabel, location
		);
	}

	@Override
	public void checkNoMappingErrorOccurred() throws ExcelImportMappingException {
		// There is no error recording.
		// Besides, if we get through to here, no error occurred.
	}

	@Override
	public void error(String error, ExcelImportLocation location) throws ExcelImportContentException {
		throw new ExcelImportContentException(error, location);
	}

	@Override
	public void missingValue(String error, ExcelImportLocation location) throws ExcelImportContentException {
		throw new ExcelImportContentException(error, location);
	}

	@Override
	public void checkNoErrorOccurred() throws ExcelImportContentException {
		// There is no error recording.
		// Besides, if we get through to here, no error occurred.
	}
	
	@Override
	public void resetErrors() {
		// No error recording here.
	}

}
