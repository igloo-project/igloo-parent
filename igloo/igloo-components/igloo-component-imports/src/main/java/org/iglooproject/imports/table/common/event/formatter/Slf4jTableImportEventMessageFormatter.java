package org.iglooproject.imports.table.common.event.formatter;

import org.iglooproject.imports.table.common.location.TableImportLocation;
import org.slf4j.helpers.MessageFormatter;

/**
 * Handles substitution of {}. Appends location formatted as {@code " (at <location.toString()>"}.
 */
public class Slf4jTableImportEventMessageFormatter implements ITableImportEventMessageFormatter {

  @Override
  public String formatHeaderLabelMappingErrorMessage(
      TableImportLocation location, String expectedHeaderLabel, int indexAmongMatchedColumns) {
    return formatErrorMessage(
        location,
        "Could not map column '{}' (index among matched columns : '{}')",
        new Object[] {expectedHeaderLabel, indexAmongMatchedColumns});
  }

  @Override
  public String formatErrorMessage(
      TableImportLocation location, String message, Object... parameters) {
    return MessageFormatter.arrayFormat(message, (Object[]) parameters).getMessage()
        + " (at "
        + location.toString()
        + ")";
  }
}
