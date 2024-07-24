package org.iglooproject.imports.table.common.mapping.column;

import org.iglooproject.functional.Function2;
import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.Predicate2;
import org.iglooproject.imports.table.common.location.ITableImportNavigator;
import org.iglooproject.imports.table.common.mapping.column.ITableImportColumnDefinition.IMappedExcelImportColumnDefinition;

public class MappedTableImportColumnDefinitionImpl<TTable, TRow, TCell, TCellReference, TValue>
    implements IMappedExcelImportColumnDefinition<TTable, TRow, TCell, TCellReference, TValue> {
  private final TTable table;
  private final boolean bound;
  private final Function2<? super TRow, ? extends TCellReference> rowToCellReferenceFunction;
  private final ITableImportNavigator<TTable, TRow, TCell, TCellReference> navigator;
  private final Function2<? super TCell, ? extends TValue> cellToValueFunction;
  private final Predicate2<? super TValue> mandatoryValuePredicate;

  public MappedTableImportColumnDefinitionImpl(
      TTable table,
      Function2<? super TRow, ? extends TCellReference> rowToCellReferenceFunction,
      ITableImportNavigator<TTable, TRow, TCell, TCellReference> navigator,
      Function2<? super TCell, ? extends TValue> cellToValueFunction,
      Predicate2<? super TValue> mandatoryValuePredicate) {
    super();
    this.table = table;
    if (rowToCellReferenceFunction != null) {
      this.bound = true;
      this.rowToCellReferenceFunction = rowToCellReferenceFunction;
    } else {
      this.bound = false;
      this.rowToCellReferenceFunction = Functions2.constant(null);
    }
    this.navigator = navigator;
    this.cellToValueFunction = cellToValueFunction;
    this.mandatoryValuePredicate = mandatoryValuePredicate;
  }

  @Override
  public boolean isBound() {
    return bound;
  }

  @Override
  public TCellReference getCellReference(TRow row) {
    return rowToCellReferenceFunction.apply(row);
  }

  @Override
  public TValue getValue(TRow row) {
    TCellReference cellReference = getCellReference(row);
    TCell cell = navigator.getCell(table, cellReference);
    return cellToValueFunction.apply(cell);
  }

  @Override
  public TValue getMandatoryValue(TRow row) {
    TValue value = getValue(row);
    if (!mandatoryValuePredicate.test(value)) {
      return null;
    }
    return value;
  }

  @Override
  public boolean hasContent(TRow row) {
    TValue value = getValue(row);
    return mandatoryValuePredicate.test(value);
  }
}
