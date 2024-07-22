package org.iglooproject.imports.table.common.event;

import org.iglooproject.imports.table.common.event.TableImportEvent.ExcelImportErrorEvent;
import org.iglooproject.imports.table.common.event.TableImportEvent.ExcelImportInfoEvent;
import org.iglooproject.imports.table.common.event.exception.TableImportContentException;
import org.iglooproject.imports.table.common.event.exception.TableImportHeaderLabelMappingException;
import org.iglooproject.imports.table.common.event.exception.TableImportMappingException;
import org.iglooproject.imports.table.common.event.formatter.ITableImportEventMessageFormatter;
import org.iglooproject.imports.table.common.location.TableImportLocation;

/**
 * A event handler that will record errors to an external sink, and may delay exception throwing
 * until {@link #checkNoErrorOccurred()} is called.
 *
 * <p><strong>CAUTION:</string> when using subclasses of this event handler, {@link
 * #checkNoErrorOccurred()} MUST be called before attempting any action based on the imported data.
 * Doing otherwise will result in corrupted data.
 */
public abstract class AbstractRecordingTableImportEventHandler implements ITableImportEventHandler {

  private final ITableImportEventMessageFormatter formatter;

  private final TableImportNonFatalErrorHandling errorHandling;

  private boolean hasError = false;

  public AbstractRecordingTableImportEventHandler(ITableImportEventMessageFormatter formatter) {
    this(TableImportNonFatalErrorHandling.THROW_ON_CHECK, formatter);
  }

  public AbstractRecordingTableImportEventHandler(
      TableImportNonFatalErrorHandling errorHandling, ITableImportEventMessageFormatter formatter) {
    this.errorHandling = errorHandling;
    this.formatter = formatter;
  }

  protected abstract void record(
      ExcelImportErrorEvent event, TableImportLocation location, String formattedMessage);

  protected abstract void record(
      ExcelImportInfoEvent event, TableImportLocation location, String formattedMessage);

  @Override
  public void headerLabelMappingError(
      String expectedHeaderLabel, int indexAmongMatchedColumns, TableImportLocation location)
      throws TableImportHeaderLabelMappingException {
    this.hasError = true;
    String formattedMessage =
        formatter.formatHeaderLabelMappingErrorMessage(
            location, expectedHeaderLabel, indexAmongMatchedColumns);
    record(TableImportEvent.ERROR, location, formattedMessage);
    if (TableImportNonFatalErrorHandling.THROW_IMMEDIATELY.equals(errorHandling)) {
      throw new TableImportHeaderLabelMappingException(
          formattedMessage, expectedHeaderLabel, location);
    }
  }

  @Override
  public void checkNoMappingErrorOccurred() throws TableImportMappingException {
    if (hasError) {
      resetErrors();
      throw new TableImportMappingException(
          "An error occurred; see the error report for more details");
    }
  }

  @Override
  public void event(
      ExcelImportErrorEvent event, TableImportLocation location, String message, Object... args)
      throws TableImportContentException {
    this.hasError = true;
    String formattedMessage = formatter.formatErrorMessage(location, message, args);
    record(event, location, formattedMessage);

    if (event.isFatal()
        || TableImportNonFatalErrorHandling.THROW_IMMEDIATELY.equals(errorHandling)) {
      throw new TableImportContentException(formattedMessage);
    }
  }

  @Override
  public void event(
      ExcelImportInfoEvent event, TableImportLocation location, String message, Object... args) {
    String formattedMessage = formatter.formatErrorMessage(location, message, args);
    record(event, location, formattedMessage);
  }

  @Override
  public void checkNoErrorOccurred() throws TableImportContentException {
    if (hasError) {
      resetErrors();
      throw new TableImportContentException(
          "An error occurred; see the error report for more details");
    }
  }

  @Override
  public void resetErrors() {
    this.hasError = false;
  }
}
