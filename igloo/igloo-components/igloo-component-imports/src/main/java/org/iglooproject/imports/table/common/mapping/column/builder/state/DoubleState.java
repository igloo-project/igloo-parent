package org.iglooproject.imports.table.common.mapping.column.builder.state;

import org.iglooproject.functional.Function2;
import org.iglooproject.functional.builder.function.generic.GenericDoubleFunctionBuildStateImpl;
import org.iglooproject.imports.table.common.mapping.AbstractTableImportColumnSet;

public abstract class DoubleState<TTable, TRow, TCell, TCellReference>
    extends GenericDoubleFunctionBuildStateImpl<
        AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference>.Column<Double>,
        ColumnFunctionBuildStateSwitcher<TTable, TRow, TCell, TCellReference, Double>,
        BooleanState<TTable, TRow, TCell, TCellReference>,
        DateState<TTable, TRow, TCell, TCellReference>,
        IntegerState<TTable, TRow, TCell, TCellReference>,
        LongState<TTable, TRow, TCell, TCellReference>,
        DoubleState<TTable, TRow, TCell, TCellReference>,
        BigDecimalState<TTable, TRow, TCell, TCellReference>,
        StringState<TTable, TRow, TCell, TCellReference>>
    implements ColumnFunctionBuildState<TTable, TRow, TCell, TCellReference, Double> {

  @Override
  public <TValue> GenericState<TTable, TRow, TCell, TCellReference, TValue> transform(
      Function2<? super Double, TValue> function) {
    return getStateSwitcher().toGeneric(function);
  }
}
