package fr.openwide.core.imports.excel.event;

import org.slf4j.Logger;

import fr.openwide.core.imports.excel.event.exception.ExcelImportContentException;
import fr.openwide.core.imports.excel.event.exception.ExcelImportHeaderLabelMappingException;
import fr.openwide.core.imports.excel.event.exception.ExcelImportMappingException;
import fr.openwide.core.imports.excel.event.formatter.IExcelImportEventMessageFormatter;
import fr.openwide.core.imports.excel.event.formatter.RawExcelImportEventMessageFormatter;
import fr.openwide.core.imports.excel.location.ExcelImportLocation;

/**
 * A event handler that will add errors to a {@link Logger} and delay exception throwing until {@link #checkNoErrorOccurred()} is called.
 * <p><strong>CAUTION:</string> when using this event handler, {@link #checkNoErrorOccurred()} MUST be called before attempting any action based on the imported data.
 * Doing otherwise will result in corrupted data.
 */
public class LoggerExcelImportEventHandler implements IExcelImportEventHandler {
	
	private final IExcelImportEventMessageFormatter formatter;
	
	private final Logger logger;
	
	private boolean hasError = false;
	
	public LoggerExcelImportEventHandler(Logger logger) {
		this(new RawExcelImportEventMessageFormatter(), logger);
	}
	
	public LoggerExcelImportEventHandler(IExcelImportEventMessageFormatter formatter, Logger logger) {
		this.formatter = formatter;
		this.logger = logger;
	}

	@Override
	public void headerLabelMappingError(String expectedHeaderLabel, int indexAmongMatchedColumns, ExcelImportLocation location) throws ExcelImportHeaderLabelMappingException {
		this.hasError = true;
		logger.error(formatter.formatHeaderLabelMappingErrorMessage(expectedHeaderLabel, indexAmongMatchedColumns, location));
		throw new ExcelImportHeaderLabelMappingException(
				"Could not map column '" + expectedHeaderLabel + "' (index among matched columns : " + indexAmongMatchedColumns + ")",
				expectedHeaderLabel, location
		);
	}

	@Override
	public void checkNoMappingErrorOccurred() throws ExcelImportMappingException {
		if (hasError) {
			this.hasError = false;
			throw new ExcelImportMappingException("An error occurred; see the logs for more details");
		}
	}

	@Override
	public void error(String error, ExcelImportLocation location) throws ExcelImportContentException {
		this.hasError = true;
		logger.error(formatter.formatErrorMessage(error, location));
	}

	@Override
	public void missingValue(String error, ExcelImportLocation location) throws ExcelImportContentException {
		this.hasError = true;
		logger.error(formatter.formatErrorMessage(error, location));
	}

	@Override
	public void checkNoErrorOccurred() throws ExcelImportContentException {
		if (hasError) {
			this.hasError = false;
			throw new ExcelImportContentException("An error occurred; see the logs for more details");
		}
	}
	
	@Override
	public void resetErrors() {
		this.hasError = false;
	}

}
