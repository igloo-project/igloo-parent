package org.iglooproject.imports.table.common.event;

import org.iglooproject.commons.util.report.BatchReport;
import org.iglooproject.imports.table.common.event.TableImportEvent.ExcelImportErrorEvent;
import org.iglooproject.imports.table.common.event.TableImportEvent.ExcelImportInfoEvent;
import org.iglooproject.imports.table.common.event.formatter.ITableImportEventMessageFormatter;
import org.iglooproject.imports.table.common.event.formatter.Slf4jTableImportEventMessageFormatter;
import org.iglooproject.imports.table.common.location.TableImportLocation;

/**
 * A event handler that will add errors to a {@link BatchReport} and delay exception throwing until
 * {@link #checkNoErrorOccurred()} is called.
 *
 * <p><strong>CAUTION:</string> when using this event handler, {@link #checkNoErrorOccurred()} MUST
 * be called before attempting any action based on the imported data. Doing otherwise will result in
 * corrupted data.
 */
public class BatchReportTableImportEventHandler extends AbstractRecordingTableImportEventHandler {

  private final BatchReport batchReport;

  public BatchReportTableImportEventHandler(BatchReport batchReport) {
    this(new Slf4jTableImportEventMessageFormatter(), batchReport);
  }

  public BatchReportTableImportEventHandler(
      ITableImportEventMessageFormatter formatter, BatchReport batchReport) {
    super(TableImportNonFatalErrorHandling.THROW_ON_CHECK, formatter);
    this.batchReport = batchReport;
  }

  @Override
  protected void record(
      ExcelImportErrorEvent event, TableImportLocation location, String formattedMessage) {
    batchReport.error(formattedMessage);
  }

  @Override
  protected void record(
      ExcelImportInfoEvent event, TableImportLocation location, String formattedMessage) {
    if (TableImportEvent.WARNING.equals(event)) {
      batchReport.warn(formattedMessage);
    } else if (TableImportEvent.DEBUG.equals(event)) {
      batchReport.debug(formattedMessage);
    } else {
      batchReport.info(formattedMessage);
    }
  }
}
