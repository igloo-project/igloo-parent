package fr.openwide.core.imports.excel.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.imports.excel.event.formatter.IExcelImportEventMessageFormatter;
import fr.openwide.core.imports.excel.event.formatter.Slf4jExcelImportEventMessageFormatter;

/**
 * A simple event handler that will throw an exception on every single error.
 * <p>No special caring is necessary when using this event handler; you may skip calling {@link #checkNoErrorOccurred()},
 * provided you properly handle exceptions.
 * @deprecated Use {@link LoggerExcelImportEventHandler} instead. It may throw exceptions immediately on errors as this handler,
 *             and offers more flexibility regarding event logging.
 */
@Deprecated
public class SimpleExcelImportEventHandler extends LoggerExcelImportEventHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleExcelImportEventHandler.class);
	
	public SimpleExcelImportEventHandler() {
		this(new Slf4jExcelImportEventMessageFormatter());
	}
	
	public SimpleExcelImportEventHandler(IExcelImportEventMessageFormatter formatter) {
		super(ExcelImportNonFatalErrorHandling.THROW_IMMEDIATELY, formatter, LOGGER);
	}

}
