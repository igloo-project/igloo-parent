package org.iglooproject.imports.table.common.mapping.column.builder.state;

import org.iglooproject.functional.Function2;
import org.iglooproject.functional.builder.function.generic.GenericIntegerFunctionBuildStateImpl;
import org.iglooproject.imports.table.common.mapping.AbstractTableImportColumnSet;

public abstract class IntegerState<TTable, TRow, TCell, TCellReference> extends GenericIntegerFunctionBuildStateImpl
		<
		AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference>.Column<Integer>,
		ColumnFunctionBuildStateSwitcher<TTable, TRow, TCell, TCellReference, Integer>,
		BooleanState<TTable, TRow, TCell, TCellReference>,
		DateState<TTable, TRow, TCell, TCellReference>,
		LocalDateState<TTable, TRow, TCell, TCellReference>,
		LocalDateTimeState<TTable, TRow, TCell, TCellReference>,
		IntegerState<TTable, TRow, TCell, TCellReference>,
		LongState<TTable, TRow, TCell, TCellReference>,
		DoubleState<TTable, TRow, TCell, TCellReference>,
		BigDecimalState<TTable, TRow, TCell, TCellReference>,
		StringState<TTable, TRow, TCell, TCellReference>
		>
		implements ColumnFunctionBuildState<TTable, TRow, TCell, TCellReference, Integer> {
		
	@Override
	public <TValue> GenericState<TTable, TRow, TCell, TCellReference, TValue> transform(Function2<? super Integer, TValue> function) {
		return getStateSwitcher().toGeneric(function);
	}

}
