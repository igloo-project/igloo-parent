package fr.openwide.core.imports.excel.event.formatter;

import fr.openwide.core.imports.excel.location.ExcelImportLocation;

public interface IExcelImportEventMessageFormatter {

	public String formatHeaderLabelMappingErrorMessage(String expectedHeaderLabel, int indexAmongMatchedColumns, ExcelImportLocation location);

	public String formatErrorMessage(String message, ExcelImportLocation location);

}
