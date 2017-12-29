package org.iglooproject.imports.table.common.mapping.column.builder.state;

import com.google.common.base.Function;

import org.iglooproject.commons.util.functional.builder.function.generic.FunctionBuildStateSwitcher;
import org.iglooproject.imports.table.common.mapping.AbstractTableImportColumnSet;

public interface ColumnFunctionBuildStateSwitcher<TTable, TRow, TCell, TCellReference, T>
		extends FunctionBuildStateSwitcher
		<
		AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference>.Column<T>,
		T,
		BooleanState<TTable, TRow, TCell, TCellReference>,
		DateState<TTable, TRow, TCell, TCellReference>,
		IntegerState<TTable, TRow, TCell, TCellReference>,
		LongState<TTable, TRow, TCell, TCellReference>,
		DoubleState<TTable, TRow, TCell, TCellReference>,
		BigDecimalState<TTable, TRow, TCell, TCellReference>,
		StringState<TTable, TRow, TCell, TCellReference>
		> {
	
	<TValue> GenericState<TTable, TRow, TCell, TCellReference, TValue> toGeneric(Function<? super T, TValue> function);

}
