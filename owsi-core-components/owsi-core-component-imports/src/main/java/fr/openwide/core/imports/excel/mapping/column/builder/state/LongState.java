package fr.openwide.core.imports.excel.mapping.column.builder.state;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.builder.function.generic.GenericLongFunctionBuildStateImpl;
import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;

public abstract class LongState<TSheet, TRow, TCell, TCellReference> extends GenericLongFunctionBuildStateImpl
		<
		AbstractExcelImportColumnSet<TSheet, TRow, TCell, TCellReference>.Column<Long>,
		ColumnFunctionBuildStateSwitcher<TSheet, TRow, TCell, TCellReference, Long>,
		BooleanState<TSheet, TRow, TCell, TCellReference>,
		DateState<TSheet, TRow, TCell, TCellReference>,
		IntegerState<TSheet, TRow, TCell, TCellReference>,
		LongState<TSheet, TRow, TCell, TCellReference>,
		DoubleState<TSheet, TRow, TCell, TCellReference>,
		StringState<TSheet, TRow, TCell, TCellReference>
		>
		implements ColumnFunctionBuildState<TSheet, TRow, TCell, TCellReference, Long> {
	
	@Override
	public <TValue> GenericState<TSheet, TRow, TCell, TCellReference, TValue> transform(Function<? super Long, TValue> function) {
		return getStateSwitcher().toGeneric(function);
	}

}
