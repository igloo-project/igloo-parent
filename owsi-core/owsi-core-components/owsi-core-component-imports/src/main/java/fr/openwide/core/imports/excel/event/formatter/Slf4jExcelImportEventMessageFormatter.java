package fr.openwide.core.imports.excel.event.formatter;

import org.slf4j.helpers.MessageFormatter;

import fr.openwide.core.imports.excel.location.ExcelImportLocation;

/**
 * Handles substitution of {}. Appends location formatted as {@code " (at <location.toString()>"}.
 */
public class Slf4jExcelImportEventMessageFormatter implements IExcelImportEventMessageFormatter {

	@Override
	public String formatHeaderLabelMappingErrorMessage(ExcelImportLocation location, String expectedHeaderLabel, int indexAmongMatchedColumns) {
		return formatErrorMessage(
				location,
				"Could not map column '{}' (index among matched columns : '{}')",
				new Object[] {expectedHeaderLabel, indexAmongMatchedColumns}
		);
	}

	@Override
	public String formatErrorMessage(ExcelImportLocation location, String message, Object ... parameters) {
		return MessageFormatter.arrayFormat(message, (Object[])parameters).getMessage() + " (at " + location.toString() + ")";
	}

}
