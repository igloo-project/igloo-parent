package fr.openwide.core.imports.excel.mapping.column.builder.state;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.Functions2;
import fr.openwide.core.commons.util.functional.builder.function.generic.GenericFunctionBuildStateImpl;
import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;

public abstract class GenericState<TSheet, TRow, TCell, TCellReference, TCurrentType> extends GenericFunctionBuildStateImpl
		<
		AbstractExcelImportColumnSet<TSheet, TRow, TCell, TCellReference>.Column<TCurrentType>,
		TCurrentType,
		ColumnFunctionBuildStateSwitcher<TSheet, TRow, TCell, TCellReference, TCurrentType>,
		BooleanState<TSheet, TRow, TCell, TCellReference>,
		DateState<TSheet, TRow, TCell, TCellReference>,
		IntegerState<TSheet, TRow, TCell, TCellReference>,
		LongState<TSheet, TRow, TCell, TCellReference>,
		DoubleState<TSheet, TRow, TCell, TCellReference>,
		BigDecimalState<TSheet, TRow, TCell, TCellReference>,
		StringState<TSheet, TRow, TCell, TCellReference>
		>
		implements ColumnFunctionBuildState<TSheet, TRow, TCell, TCellReference, TCurrentType> {

	@Override
	public AbstractExcelImportColumnSet<TSheet, TRow, TCell, TCellReference>.Column<TCurrentType> withDefault(TCurrentType defaultValue) {
		return transform(Functions2.defaultValue(defaultValue)).build();
	}
	
	@Override
	public <TValue> GenericState<TSheet, TRow, TCell, TCellReference, TValue> transform(Function<? super TCurrentType, TValue> function) {
		return getStateSwitcher().toGeneric(function);
	}

}
