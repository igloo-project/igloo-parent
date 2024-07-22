package org.iglooproject.imports.table.common.event.formatter;

import org.iglooproject.imports.table.common.location.TableImportLocation;

public interface ITableImportEventMessageFormatter {

  public String formatHeaderLabelMappingErrorMessage(
      TableImportLocation location, String expectedHeaderLabel, int indexAmongMatchedColumns);

  public String formatErrorMessage(
      TableImportLocation location, String message, Object... parameters);
}
