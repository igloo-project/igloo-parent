package org.iglooproject.imports.table.common.mapping.column.builder.state;

import java.time.LocalDateTime;

import org.iglooproject.functional.Function2;
import org.iglooproject.functional.builder.function.generic.GenericLocalDateTimeBuildStateImpl;
import org.iglooproject.imports.table.common.mapping.AbstractTableImportColumnSet;

public abstract class LocalDateTimeState<TTable, TRow, TCell, TCellReference> extends GenericLocalDateTimeBuildStateImpl
		<
		AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference>.Column<LocalDateTime>,
		ColumnFunctionBuildStateSwitcher<TTable, TRow, TCell, TCellReference, LocalDateTime>,
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
		implements ColumnFunctionBuildState<TTable, TRow, TCell, TCellReference, LocalDateTime> {
		
	@Override
	public <TValue> GenericState<TTable, TRow, TCell, TCellReference, TValue> transform(Function2<? super LocalDateTime, TValue> function) {
		return getStateSwitcher().toGeneric(function);
	}

}
