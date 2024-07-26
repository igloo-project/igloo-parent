package org.iglooproject.imports.table.common.mapping.column;

import org.iglooproject.imports.table.common.event.ITableImportEventHandler;
import org.iglooproject.imports.table.common.event.exception.TableImportMappingException;
import org.iglooproject.imports.table.common.location.ITableImportNavigator;

public interface ITableImportColumnDefinition<TSheet, TRow, TCell, TCellReference, TValue> {

  IMappedExcelImportColumnDefinition<TSheet, TRow, TCell, TCellReference, TValue> map(
      TSheet sheet,
      ITableImportNavigator<TSheet, TRow, TCell, TCellReference> navigator,
      ITableImportEventHandler eventHandler)
      throws TableImportMappingException;

  interface IMappedExcelImportColumnDefinition<TSheet, TRow, TCell, TCellReference, TValue> {

    /**
     * @return True if this "mapped" column is actually bound to a column in the sheet, false
     *     otherwise.
     */
    boolean isBound();

    TCellReference getCellReference(TRow row);

    TValue getValue(TRow row);

    /**
     * @return The cell value, or null if the value does not satisfy "mandatory requirements" for
     *     the value type.
     *     <p>In general, a value is assumed "present" if it is not {@code null}, so this method
     *     returns exactly the same as {@link #getValue(Object)}. In some cases though, the result
     *     may vary. For instance, when dealing whith strings, an empty string would be considered
     *     as "absent", so that {@link #getValue(Object)} would return {@code ""} and {@link
     *     #getMandatoryValue(Object)} would return {@code null}.
     */
    TValue getMandatoryValue(TRow row);

    /**
     * @return True if the value satisfies "mandatory requirements" for the value type, false
     *     otherwise.
     *     <p>In general, this means that the value is not null. In some cases though, the value
     *     must respect some additional requirements. For instance, when dealing whith strings, a
     *     cell containing an empty string would be considered as having no content.
     */
    boolean hasContent(TRow row);
  }
}
