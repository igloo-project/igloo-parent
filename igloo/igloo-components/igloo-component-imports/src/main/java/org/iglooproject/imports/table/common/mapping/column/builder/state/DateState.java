package org.iglooproject.imports.table.common.mapping.column.builder.state;

import java.util.Date;

import com.google.common.base.Function;

import org.iglooproject.commons.util.functional.builder.function.generic.GenericDateBuildStateImpl;
import org.iglooproject.imports.table.common.mapping.AbstractTableImportColumnSet;

public abstract class DateState<TTable, TRow, TCell, TCellReference> extends GenericDateBuildStateImpl
		<
		AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference>.Column<Date>,
		ColumnFunctionBuildStateSwitcher<TTable, TRow, TCell, TCellReference, Date>,
		BooleanState<TTable, TRow, TCell, TCellReference>,
		DateState<TTable, TRow, TCell, TCellReference>,
		IntegerState<TTable, TRow, TCell, TCellReference>,
		LongState<TTable, TRow, TCell, TCellReference>,
		DoubleState<TTable, TRow, TCell, TCellReference>,
		BigDecimalState<TTable, TRow, TCell, TCellReference>,
		StringState<TTable, TRow, TCell, TCellReference>
		>
		implements ColumnFunctionBuildState<TTable, TRow, TCell, TCellReference, Date> {
		
	@Override
	public <TValue> GenericState<TTable, TRow, TCell, TCellReference, TValue> transform(Function<? super Date, TValue> function) {
		return getStateSwitcher().toGeneric(function);
	}

}
