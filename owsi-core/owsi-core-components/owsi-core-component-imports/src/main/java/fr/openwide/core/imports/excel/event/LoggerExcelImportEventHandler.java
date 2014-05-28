package fr.openwide.core.imports.excel.event;

import org.slf4j.Logger;

import fr.openwide.core.imports.excel.event.ExcelImportEvent.ExcelImportErrorEvent;
import fr.openwide.core.imports.excel.event.ExcelImportEvent.ExcelImportInfoEvent;
import fr.openwide.core.imports.excel.event.formatter.IExcelImportEventMessageFormatter;
import fr.openwide.core.imports.excel.event.formatter.Slf4jExcelImportEventMessageFormatter;
import fr.openwide.core.imports.excel.location.ExcelImportLocation;

/**
 * A event handler that will add errors to a {@link Logger} and may (or may not, depending on constructor parameters) delay exception throwing until {@link #checkNoErrorOccurred()} is called.
 * <p><strong>CAUTION:</string> when using this event handler, {@link #checkNoErrorOccurred()} MUST be called before attempting any action based on the imported data.
 * Doing otherwise will result in corrupted data.
 */
public class LoggerExcelImportEventHandler extends AbstractRecordingExcelImportEventHandler {
	
	private final Logger logger;
	
	public LoggerExcelImportEventHandler(Logger logger) {
		this(new Slf4jExcelImportEventMessageFormatter(), logger);
	}
	
	public LoggerExcelImportEventHandler(ExcelImportNonFatalErrorHandling errorHandling, Logger logger) {
		this(errorHandling, new Slf4jExcelImportEventMessageFormatter(), logger);
	}
	
	public LoggerExcelImportEventHandler(IExcelImportEventMessageFormatter formatter, Logger logger) {
		this(ExcelImportNonFatalErrorHandling.THROW_ON_CHECK, formatter, logger);
	}
	
	public LoggerExcelImportEventHandler(ExcelImportNonFatalErrorHandling errorHandling, IExcelImportEventMessageFormatter formatter, Logger logger) {
		super(errorHandling, formatter);
		this.logger = logger;
	}
	
	@Override
	protected void record(ExcelImportErrorEvent event, ExcelImportLocation location, String formattedMessage) {
		logger.error(formattedMessage);
	}
	
	@Override
	protected void record(ExcelImportInfoEvent event, ExcelImportLocation location, String formattedMessage) {
		if (ExcelImportEvent.WARNING.equals(event)) {
			logger.warn(formattedMessage);
		} else {
			logger.info(formattedMessage);
		}
	}

}
