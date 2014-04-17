package fr.openwide.core.imports.excel.mapping.column.builder.state;

import com.google.common.base.Function;

public interface ColumnFunctionBuildState<TSheet, TRow, TCell, TCellReference, TCurrentType> {
	
	<TValue> GenericState<TSheet, TRow, TCell, TCellReference, TValue> transform(Function<? super TCurrentType, TValue> function);

}
