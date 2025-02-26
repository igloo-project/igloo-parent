package org.iglooproject.imports.table.common.mapping.column.builder.state;

import org.iglooproject.functional.Function2;
import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.builder.function.generic.GenericFunctionBuildStateImpl;
import org.iglooproject.imports.table.common.mapping.AbstractTableImportColumnSet;

public abstract class GenericState<TTable, TRow, TCell, TCellReference, TCurrentType>
    extends GenericFunctionBuildStateImpl<
        AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference>.Column<TCurrentType>,
        TCurrentType,
        ColumnFunctionBuildStateSwitcher<TTable, TRow, TCell, TCellReference, TCurrentType>,
        BooleanState<TTable, TRow, TCell, TCellReference>,
        DateState<TTable, TRow, TCell, TCellReference>,
        LocalDateState<TTable, TRow, TCell, TCellReference>,
        LocalDateTimeState<TTable, TRow, TCell, TCellReference>,
        IntegerState<TTable, TRow, TCell, TCellReference>,
        LongState<TTable, TRow, TCell, TCellReference>,
        DoubleState<TTable, TRow, TCell, TCellReference>,
        BigDecimalState<TTable, TRow, TCell, TCellReference>,
        StringState<TTable, TRow, TCell, TCellReference>>
    implements ColumnFunctionBuildState<TTable, TRow, TCell, TCellReference, TCurrentType> {

  @Override
  public AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference>.Column<TCurrentType>
      withDefault(TCurrentType defaultValue) {
    return transform(Functions2.defaultValue(defaultValue)).build();
  }

  @Override
  public <TValue> GenericState<TTable, TRow, TCell, TCellReference, TValue> transform(
      Function2<? super TCurrentType, TValue> function) {
    return getStateSwitcher().toGeneric(function);
  }
}
