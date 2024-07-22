package org.iglooproject.imports.table.common.event;

import org.iglooproject.imports.table.common.event.TableImportEvent.ExcelImportErrorEvent;
import org.iglooproject.imports.table.common.event.TableImportEvent.ExcelImportInfoEvent;
import org.iglooproject.imports.table.common.event.formatter.ITableImportEventMessageFormatter;
import org.iglooproject.imports.table.common.event.formatter.Slf4jTableImportEventMessageFormatter;
import org.iglooproject.imports.table.common.location.TableImportLocation;
import org.slf4j.Logger;

/**
 * A event handler that will add errors to a {@link Logger} and may (or may not, depending on
 * constructor parameters) delay exception throwing until {@link #checkNoErrorOccurred()} is called.
 *
 * <p><strong>CAUTION:</string> when using this event handler, {@link #checkNoErrorOccurred()} MUST
 * be called before attempting any action based on the imported data. Doing otherwise will result in
 * corrupted data.
 */
public class LoggerTableImportEventHandler extends AbstractRecordingTableImportEventHandler {

  private final Logger logger;

  public LoggerTableImportEventHandler(Logger logger) {
    this(new Slf4jTableImportEventMessageFormatter(), logger);
  }

  public LoggerTableImportEventHandler(
      TableImportNonFatalErrorHandling errorHandling, Logger logger) {
    this(errorHandling, new Slf4jTableImportEventMessageFormatter(), logger);
  }

  public LoggerTableImportEventHandler(ITableImportEventMessageFormatter formatter, Logger logger) {
    this(TableImportNonFatalErrorHandling.THROW_ON_CHECK, formatter, logger);
  }

  public LoggerTableImportEventHandler(
      TableImportNonFatalErrorHandling errorHandling,
      ITableImportEventMessageFormatter formatter,
      Logger logger) {
    super(errorHandling, formatter);
    this.logger = logger;
  }

  @Override
  protected void record(
      ExcelImportErrorEvent event, TableImportLocation location, String formattedMessage) {
    logger.error(formattedMessage);
  }

  @Override
  protected void record(
      ExcelImportInfoEvent event, TableImportLocation location, String formattedMessage) {
    if (TableImportEvent.WARNING.equals(event)) {
      logger.warn(formattedMessage);
    } else if (TableImportEvent.DEBUG.equals(event)) {
      logger.debug(formattedMessage);
    } else {
      logger.info(formattedMessage);
    }
  }
}
