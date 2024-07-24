package org.iglooproject.imports.table.common.location;

import org.iglooproject.imports.table.common.event.ITableImportEventHandler;
import org.iglooproject.imports.table.common.event.TableImportEvent;
import org.iglooproject.imports.table.common.event.TableImportEvent.ExcelImportErrorEvent;
import org.iglooproject.imports.table.common.event.TableImportEvent.ExcelImportInfoEvent;
import org.iglooproject.imports.table.common.event.exception.TableImportContentException;

public abstract class TableImportLocationContext {

  protected final ITableImportEventHandler eventHandler;

  protected TableImportLocationContext(ITableImportEventHandler eventHandler) {
    super();
    this.eventHandler = eventHandler;
  }

  public final void fatal(String message, Object... args) throws TableImportContentException {
    event(TableImportEvent.FATAL, message, (Object[]) args);
  }

  public final void error(String message, Object... args) throws TableImportContentException {
    event(TableImportEvent.ERROR, message, (Object[]) args);
  }

  public void event(ExcelImportErrorEvent event, String message, Object... args)
      throws TableImportContentException {
    eventHandler.event(event, getLocation(), message, (Object[]) args);
  }

  public final void warn(String message, Object... args) {
    event(TableImportEvent.WARNING, message, (Object[]) args);
  }

  public final void info(String message, Object... args) {
    event(TableImportEvent.INFO, message, (Object[]) args);
  }

  public final void debug(String message, Object... args) {
    event(TableImportEvent.DEBUG, message, (Object[]) args);
  }

  public void event(ExcelImportInfoEvent event, String message, Object... args) {
    eventHandler.event(event, getLocation(), message, (Object[]) args);
  }

  public abstract TableImportLocation getLocation();
}
