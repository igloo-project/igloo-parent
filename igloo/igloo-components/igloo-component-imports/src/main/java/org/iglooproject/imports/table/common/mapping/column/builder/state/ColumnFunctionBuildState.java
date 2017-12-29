package org.iglooproject.imports.table.common.mapping.column.builder.state;

import com.google.common.base.Function;

public interface ColumnFunctionBuildState<TTable, TRow, TCell, TCellReference, TCurrentType> {
	
	<TValue> GenericState<TTable, TRow, TCell, TCellReference, TValue> transform(Function<? super TCurrentType, TValue> function);

}
