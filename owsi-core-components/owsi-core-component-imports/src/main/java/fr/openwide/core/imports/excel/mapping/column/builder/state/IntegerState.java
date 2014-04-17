package fr.openwide.core.imports.excel.mapping.column.builder.state;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.builder.function.generic.GenericIntegerFunctionBuildStateImpl;
import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;

public abstract class IntegerState<TSheet, TRow, TCell, TCellReference> extends GenericIntegerFunctionBuildStateImpl
		<
		AbstractExcelImportColumnSet<TSheet, TRow, TCell, TCellReference>.Column<Integer>,
		ColumnFunctionBuildStateSwitcher<TSheet, TRow, TCell, TCellReference, Integer>,
		BooleanState<TSheet, TRow, TCell, TCellReference>,
		DateState<TSheet, TRow, TCell, TCellReference>,
		IntegerState<TSheet, TRow, TCell, TCellReference>,
		LongState<TSheet, TRow, TCell, TCellReference>,
		DoubleState<TSheet, TRow, TCell, TCellReference>,
		StringState<TSheet, TRow, TCell, TCellReference>
		>
		implements ColumnFunctionBuildState<TSheet, TRow, TCell, TCellReference, Integer> {
		
	@Override
	public <TValue> GenericState<TSheet, TRow, TCell, TCellReference, TValue> transform(Function<? super Integer, TValue> function) {
		return getStateSwitcher().toGeneric(function);
	}

}
