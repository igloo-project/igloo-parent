package fr.openwide.core.imports.excel.event;

import fr.openwide.core.commons.util.report.BatchReport;
import fr.openwide.core.imports.excel.event.ExcelImportEvent.ExcelImportErrorEvent;
import fr.openwide.core.imports.excel.event.ExcelImportEvent.ExcelImportInfoEvent;
import fr.openwide.core.imports.excel.event.formatter.IExcelImportEventMessageFormatter;
import fr.openwide.core.imports.excel.event.formatter.Slf4jExcelImportEventMessageFormatter;
import fr.openwide.core.imports.excel.location.ExcelImportLocation;

/**
 * A event handler that will add errors to a {@link BatchReport} and delay exception throwing until {@link #checkNoErrorOccurred()} is called.
 * <p><strong>CAUTION:</string> when using this event handler, {@link #checkNoErrorOccurred()} MUST be called before attempting any action based on the imported data.
 * Doing otherwise will result in corrupted data.
 */
public class BatchReportExcelImportEventHandler extends AbstractRecordingExcelImportEventHandler {
	
	private final BatchReport batchReport;
	
	public BatchReportExcelImportEventHandler(BatchReport batchReport) {
		this(new Slf4jExcelImportEventMessageFormatter(), batchReport);
	}
	
	public BatchReportExcelImportEventHandler(IExcelImportEventMessageFormatter formatter, BatchReport batchReport) {
		super(ExcelImportNonFatalErrorHandling.THROW_ON_CHECK, formatter);
		this.batchReport = batchReport;
	}

	@Override
	protected void record(ExcelImportErrorEvent event, ExcelImportLocation location, String formattedMessage) {
		batchReport.error(formattedMessage);
	}

	@Override
	protected void record(ExcelImportInfoEvent event, ExcelImportLocation location, String formattedMessage) {
		if (ExcelImportEvent.WARNING.equals(event)) {
			batchReport.warn(formattedMessage);
		} else if (ExcelImportEvent.DEBUG.equals(event)) {
			batchReport.debug(formattedMessage);
		} else {
			batchReport.info(formattedMessage);
		}
	}

}
