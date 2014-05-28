package fr.openwide.core.imports.excel.event;

import fr.openwide.core.imports.excel.event.ExcelImportEvent.ExcelImportErrorEvent;
import fr.openwide.core.imports.excel.event.ExcelImportEvent.ExcelImportInfoEvent;
import fr.openwide.core.imports.excel.event.exception.ExcelImportContentException;
import fr.openwide.core.imports.excel.event.exception.ExcelImportHeaderLabelMappingException;
import fr.openwide.core.imports.excel.event.exception.ExcelImportMappingException;
import fr.openwide.core.imports.excel.event.formatter.IExcelImportEventMessageFormatter;
import fr.openwide.core.imports.excel.location.ExcelImportLocation;

/**
 * A event handler that will record errors to an external sink, and may delay exception throwing until {@link #checkNoErrorOccurred()} is called.
 * <p><strong>CAUTION:</string> when using subclasses of this event handler, {@link #checkNoErrorOccurred()} MUST be called before attempting any action based on the imported data.
 * Doing otherwise will result in corrupted data.
 */
public abstract class AbstractRecordingExcelImportEventHandler implements IExcelImportEventHandler {
	
	private final IExcelImportEventMessageFormatter formatter;
	
	private final ExcelImportNonFatalErrorHandling errorHandling;
	
	private boolean hasError = false;
	
	public AbstractRecordingExcelImportEventHandler(IExcelImportEventMessageFormatter formatter) {
		this(ExcelImportNonFatalErrorHandling.THROW_ON_CHECK, formatter);
	}
	
	public AbstractRecordingExcelImportEventHandler(ExcelImportNonFatalErrorHandling errorHandling, IExcelImportEventMessageFormatter formatter) {
		this.errorHandling = errorHandling;
		this.formatter = formatter;
	}

	protected abstract void record(ExcelImportErrorEvent event, ExcelImportLocation location, String formattedMessage);

	protected abstract void record(ExcelImportInfoEvent event, ExcelImportLocation location, String formattedMessage);

	@Override
	public void headerLabelMappingError(String expectedHeaderLabel, int indexAmongMatchedColumns, ExcelImportLocation location) throws ExcelImportHeaderLabelMappingException {
		this.hasError = true;
		String formattedMessage = formatter.formatHeaderLabelMappingErrorMessage(location, expectedHeaderLabel, indexAmongMatchedColumns);
		record(ExcelImportEvent.ERROR, location, formattedMessage);
		if (ExcelImportNonFatalErrorHandling.THROW_IMMEDIATELY.equals(errorHandling)) {
			throw new ExcelImportHeaderLabelMappingException(formattedMessage, expectedHeaderLabel, location);
		}
	}

	@Override
	public void checkNoMappingErrorOccurred() throws ExcelImportMappingException {
		if (hasError) {
			resetErrors();
			throw new ExcelImportMappingException("An error occurred; see the error report for more details");
		}
	}
	
	@Override
	public void event(ExcelImportErrorEvent event, ExcelImportLocation location, String message, Object ... args)
			throws ExcelImportContentException {
		this.hasError = true;
		String formattedMessage = formatter.formatErrorMessage(location, message, args);
		record(event, location, formattedMessage);
		
		if (event.isFatal() || ExcelImportNonFatalErrorHandling.THROW_IMMEDIATELY.equals(errorHandling)) {
			throw new ExcelImportContentException(formattedMessage);
		}
	}
	
	@Override
	public void event(ExcelImportInfoEvent event, ExcelImportLocation location, String message, Object ... args) {
		String formattedMessage = formatter.formatErrorMessage(location, message, args);
		record(event, location, formattedMessage);
	}
	

	@Override
	public void checkNoErrorOccurred() throws ExcelImportContentException {
		if (hasError) {
			resetErrors();
			throw new ExcelImportContentException("An error occurred; see the error report for more details");
		}
	}
	
	@Override
	public void resetErrors() {
		this.hasError = false;
	}

}
