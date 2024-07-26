package org.iglooproject.imports.table.common.mapping.column.builder.state;

import org.iglooproject.functional.Function2;
import org.iglooproject.functional.builder.function.generic.GenericStringFunctionBuildStateImpl;
import org.iglooproject.imports.table.common.mapping.AbstractTableImportColumnSet;

public abstract class StringState<TTable, TRow, TCell, TCellReference>
    extends GenericStringFunctionBuildStateImpl<
        AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference>.Column<String>,
        ColumnFunctionBuildStateSwitcher<TTable, TRow, TCell, TCellReference, String>,
        BooleanState<TTable, TRow, TCell, TCellReference>,
        DateState<TTable, TRow, TCell, TCellReference>,
        IntegerState<TTable, TRow, TCell, TCellReference>,
        LongState<TTable, TRow, TCell, TCellReference>,
        DoubleState<TTable, TRow, TCell, TCellReference>,
        BigDecimalState<TTable, TRow, TCell, TCellReference>,
        StringState<TTable, TRow, TCell, TCellReference>>
    implements ColumnFunctionBuildState<TTable, TRow, TCell, TCellReference, String> {

  @Override
  public <TValue> GenericState<TTable, TRow, TCell, TCellReference, TValue> transform(
      Function2<? super String, TValue> function) {
    return getStateSwitcher().toGeneric(function);
  }
}
