package org.iglooproject.imports.table.common.mapping.column.builder.state;

import org.iglooproject.functional.Function2;

public interface ColumnFunctionBuildState<TTable, TRow, TCell, TCellReference, TCurrentType> {

  <TValue> GenericState<TTable, TRow, TCell, TCellReference, TValue> transform(
      Function2<? super TCurrentType, TValue> function);
}
