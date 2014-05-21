package fr.openwide.core.imports.excel.mapping.column.builder.state;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.builder.function.generic.GenericBooleanFunctionBuildStateImpl;
import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;

public abstract class BooleanState<TSheet, TRow, TCell, TCellReference> extends GenericBooleanFunctionBuildStateImpl
		<
		AbstractExcelImportColumnSet<TSheet, TRow, TCell, TCellReference>.Column<Boolean>,
		ColumnFunctionBuildStateSwitcher<TSheet, TRow, TCell, TCellReference, Boolean>,
		BooleanState<TSheet, TRow, TCell, TCellReference>,
		DateState<TSheet, TRow, TCell, TCellReference>,
		IntegerState<TSheet, TRow, TCell, TCellReference>,
		LongState<TSheet, TRow, TCell, TCellReference>,
		DoubleState<TSheet, TRow, TCell, TCellReference>,
		BigDecimalState<TSheet, TRow, TCell, TCellReference>,
		StringState<TSheet, TRow, TCell, TCellReference>
		>
		implements ColumnFunctionBuildState<TSheet, TRow, TCell, TCellReference, Boolean> {
	
	@Override
	public <TValue> GenericState<TSheet, TRow, TCell, TCellReference, TValue> transform(Function<? super Boolean, TValue> function) {
		return getStateSwitcher().toGeneric(function);
	}

}
