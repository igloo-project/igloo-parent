package org.iglooproject.imports.table.common.event;

import org.iglooproject.imports.table.common.event.TableImportEvent.ExcelImportErrorEvent;
import org.iglooproject.imports.table.common.event.TableImportEvent.ExcelImportInfoEvent;
import org.iglooproject.imports.table.common.event.exception.TableImportContentException;
import org.iglooproject.imports.table.common.event.exception.TableImportHeaderLabelMappingException;
import org.iglooproject.imports.table.common.event.exception.TableImportMappingException;
import org.iglooproject.imports.table.common.location.TableImportLocation;

/**
 * An interface for classes handling excel import events (mainly errors for the moment).
 *
 * <p>This class assumes that excel import include error-tolerant phases (for instance, when doing
 * only data loading), and phases where no error can be tolerated (for instance, when using the
 * loaded data are used to update database entities). That's the point of the {@link
 * #checkNoErrorOccurred()} method: implementors may choose to silently stack errors when {@link
 * #error(String, TableImportLocation)} or {@link #missingValue(String, TableImportLocation)} are
 * called, and delay exception throwing until {@link #checkNoErrorOccurred()} is called.
 */
public interface ITableImportEventHandler {

  void headerLabelMappingError(
      String expectedHeaderLabel, int indexAmongMatchedColumns, TableImportLocation location)
      throws TableImportHeaderLabelMappingException;

  void checkNoMappingErrorOccurred() throws TableImportMappingException;

  void event(
      ExcelImportErrorEvent event, TableImportLocation location, String message, Object... args)
      throws TableImportContentException;

  void event(
      ExcelImportInfoEvent event, TableImportLocation location, String message, Object... args);

  /**
   * Checks that no error occurred since the last call to {@link #checkNoErrorOccurred()} or {@link
   * #resetErrors()}, throwing an exception if an error has actually occurred.
   */
  void checkNoErrorOccurred() throws TableImportContentException;

  void resetErrors();
}
