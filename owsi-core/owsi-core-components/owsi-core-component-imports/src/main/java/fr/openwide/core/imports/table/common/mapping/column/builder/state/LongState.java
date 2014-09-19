package fr.openwide.core.imports.table.common.mapping.column.builder.state;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.builder.function.generic.GenericLongFunctionBuildStateImpl;
import fr.openwide.core.imports.table.common.mapping.AbstractTableImportColumnSet;

public abstract class LongState<TTable, TRow, TCell, TCellReference> extends GenericLongFunctionBuildStateImpl
		<
		AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference>.Column<Long>,
		ColumnFunctionBuildStateSwitcher<TTable, TRow, TCell, TCellReference, Long>,
		BooleanState<TTable, TRow, TCell, TCellReference>,
		DateState<TTable, TRow, TCell, TCellReference>,
		IntegerState<TTable, TRow, TCell, TCellReference>,
		LongState<TTable, TRow, TCell, TCellReference>,
		DoubleState<TTable, TRow, TCell, TCellReference>,
		BigDecimalState<TTable, TRow, TCell, TCellReference>,
		StringState<TTable, TRow, TCell, TCellReference>
		>
		implements ColumnFunctionBuildState<TTable, TRow, TCell, TCellReference, Long> {
	
	@Override
	public <TValue> GenericState<TTable, TRow, TCell, TCellReference, TValue> transform(Function<? super Long, TValue> function) {
		return getStateSwitcher().toGeneric(function);
	}

}
