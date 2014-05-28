package fr.openwide.core.imports.excel.location;

import fr.openwide.core.imports.excel.event.ExcelImportEvent;
import fr.openwide.core.imports.excel.event.ExcelImportEvent.ExcelImportErrorEvent;
import fr.openwide.core.imports.excel.event.ExcelImportEvent.ExcelImportInfoEvent;
import fr.openwide.core.imports.excel.event.exception.ExcelImportContentException;

public abstract class ExcelImportLocationContext {

	public final void fatal(String message, Object ... args) throws ExcelImportContentException {
		event(ExcelImportEvent.FATAL, message, (Object[])args);
	}

	public final void error(String message, Object ... args) throws ExcelImportContentException {
		event(ExcelImportEvent.ERROR, message, (Object[])args);
	}
	
	public abstract void event(ExcelImportErrorEvent event, String message, Object ... args) throws ExcelImportContentException;

	public final void warn(String message, Object ... args) {
		event(ExcelImportEvent.WARNING, message, (Object[])args);
	}

	public final void info(String message, Object ... args) {
		event(ExcelImportEvent.INFO, message, (Object[])args);
	}
	
	public abstract void event(ExcelImportInfoEvent event, String message, Object ... args);

}
