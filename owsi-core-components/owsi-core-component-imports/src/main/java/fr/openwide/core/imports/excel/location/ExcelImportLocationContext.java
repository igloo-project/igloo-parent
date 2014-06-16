package fr.openwide.core.imports.excel.location;

import fr.openwide.core.imports.excel.event.ExcelImportEvent;
import fr.openwide.core.imports.excel.event.IExcelImportEventHandler;
import fr.openwide.core.imports.excel.event.ExcelImportEvent.ExcelImportErrorEvent;
import fr.openwide.core.imports.excel.event.ExcelImportEvent.ExcelImportInfoEvent;
import fr.openwide.core.imports.excel.event.exception.ExcelImportContentException;

public abstract class ExcelImportLocationContext {
	
	protected final IExcelImportEventHandler eventHandler;

	protected ExcelImportLocationContext(IExcelImportEventHandler eventHandler) {
		super();
		this.eventHandler = eventHandler;
	}

	public final void fatal(String message, Object ... args) throws ExcelImportContentException {
		event(ExcelImportEvent.FATAL, message, (Object[])args);
	}

	public final void error(String message, Object ... args) throws ExcelImportContentException {
		event(ExcelImportEvent.ERROR, message, (Object[])args);
	}
	
	public void event(ExcelImportErrorEvent event, String message, Object ... args) throws ExcelImportContentException {
		eventHandler.event(event, getLocation(), message, (Object[])args);
	}

	public final void warn(String message, Object ... args) {
		event(ExcelImportEvent.WARNING, message, (Object[])args);
	}

	public final void info(String message, Object ... args) {
		event(ExcelImportEvent.INFO, message, (Object[])args);
	}
	
	public void event(ExcelImportInfoEvent event, String message, Object ... args) {
		eventHandler.event(event, getLocation(), message, (Object[])args);
	}
	
	public abstract ExcelImportLocation getLocation();

}
