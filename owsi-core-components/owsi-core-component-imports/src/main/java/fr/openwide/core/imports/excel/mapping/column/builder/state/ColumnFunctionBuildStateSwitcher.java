package fr.openwide.core.imports.excel.mapping.column.builder.state;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.builder.function.generic.FunctionBuildStateSwitcher;
import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;

public interface ColumnFunctionBuildStateSwitcher<TSheet, TRow, TCell, TCellReference, T>
		extends FunctionBuildStateSwitcher
		<
		AbstractExcelImportColumnSet<TSheet, TRow, TCell, TCellReference>.Column<T>,
		T,
		BooleanState<TSheet, TRow, TCell, TCellReference>,
		DateState<TSheet, TRow, TCell, TCellReference>,
		IntegerState<TSheet, TRow, TCell, TCellReference>,
		LongState<TSheet, TRow, TCell, TCellReference>,
		DoubleState<TSheet, TRow, TCell, TCellReference>,
		StringState<TSheet, TRow, TCell, TCellReference>
		> {
	
	<TValue> GenericState<TSheet, TRow, TCell, TCellReference, TValue> toGeneric(Function<? super T, TValue> function);

}
