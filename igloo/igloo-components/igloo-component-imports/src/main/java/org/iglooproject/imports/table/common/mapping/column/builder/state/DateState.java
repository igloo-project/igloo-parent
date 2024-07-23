package org.iglooproject.imports.table.common.mapping.column.builder.state;

import java.util.Date;
import org.iglooproject.functional.Function2;
import org.iglooproject.functional.builder.function.generic.GenericDateBuildStateImpl;
import org.iglooproject.imports.table.common.mapping.AbstractTableImportColumnSet;

public abstract class DateState<TTable, TRow, TCell, TCellReference>
    extends GenericDateBuildStateImpl<
        AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference>.Column<Date>,
        ColumnFunctionBuildStateSwitcher<TTable, TRow, TCell, TCellReference, Date>,
        BooleanState<TTable, TRow, TCell, TCellReference>,
        DateState<TTable, TRow, TCell, TCellReference>,
        IntegerState<TTable, TRow, TCell, TCellReference>,
        LongState<TTable, TRow, TCell, TCellReference>,
        DoubleState<TTable, TRow, TCell, TCellReference>,
        BigDecimalState<TTable, TRow, TCell, TCellReference>,
        StringState<TTable, TRow, TCell, TCellReference>>
    implements ColumnFunctionBuildState<TTable, TRow, TCell, TCellReference, Date> {

  @Override
  public <TValue> GenericState<TTable, TRow, TCell, TCellReference, TValue> transform(
      Function2<? super Date, TValue> function) {
    return getStateSwitcher().toGeneric(function);
  }
}
