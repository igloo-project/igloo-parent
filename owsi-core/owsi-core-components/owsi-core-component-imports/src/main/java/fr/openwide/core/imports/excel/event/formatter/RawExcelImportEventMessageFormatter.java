package fr.openwide.core.imports.excel.event.formatter;

import fr.openwide.core.imports.excel.location.ExcelImportLocation;

public class RawExcelImportEventMessageFormatter implements IExcelImportEventMessageFormatter {

	@Override
	public String formatHeaderLabelMappingErrorMessage(String expectedHeaderLabel, int indexAmongMatchedColumns, ExcelImportLocation location) {
		return formatErrorMessage(
				"Could not map column '" + expectedHeaderLabel + "' (index among matched columns : " + indexAmongMatchedColumns + ")",
				location
		);
	}

	@Override
	public String formatErrorMessage(String message, ExcelImportLocation location) {
		return message + " (at location " + location + ")";
	}

}
