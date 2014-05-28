package fr.openwide.core.imports.excel.event.formatter;

import fr.openwide.core.imports.excel.location.ExcelImportLocation;

public interface IExcelImportEventMessageFormatter {

	public String formatHeaderLabelMappingErrorMessage(ExcelImportLocation location, String expectedHeaderLabel, int indexAmongMatchedColumns);

	public String formatErrorMessage(ExcelImportLocation location, String message, Object ... parameters);

}
