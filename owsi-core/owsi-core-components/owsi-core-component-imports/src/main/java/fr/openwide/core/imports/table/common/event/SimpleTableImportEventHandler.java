package fr.openwide.core.imports.table.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.openwide.core.imports.table.common.event.formatter.ITableImportEventMessageFormatter;
import fr.openwide.core.imports.table.common.event.formatter.Slf4jTableImportEventMessageFormatter;

/**
 * A simple event handler that will throw an exception on every single error.
 * <p>No special caring is necessary when using this event handler; you may skip calling {@link #checkNoErrorOccurred()},
 * provided you properly handle exceptions.
 * @deprecated Use {@link LoggerTableImportEventHandler} instead. It may throw exceptions immediately on errors as this handler,
 *             and offers more flexibility regarding event logging.
 */
@Deprecated
public class SimpleTableImportEventHandler extends LoggerTableImportEventHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTableImportEventHandler.class);
	
	public SimpleTableImportEventHandler() {
		this(new Slf4jTableImportEventMessageFormatter());
	}
	
	public SimpleTableImportEventHandler(ITableImportEventMessageFormatter formatter) {
		super(TableImportNonFatalErrorHandling.THROW_IMMEDIATELY, formatter, LOGGER);
	}

}
