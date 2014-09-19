package fr.openwide.core.imports.table.common.event.formatter;

import fr.openwide.core.imports.table.common.location.TableImportLocation;


public interface ITableImportEventMessageFormatter {

	public String formatHeaderLabelMappingErrorMessage(TableImportLocation location, String expectedHeaderLabel, int indexAmongMatchedColumns);

	public String formatErrorMessage(TableImportLocation location, String message, Object ... parameters);

}
