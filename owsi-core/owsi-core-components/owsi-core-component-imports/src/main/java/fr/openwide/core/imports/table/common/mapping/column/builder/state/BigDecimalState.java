package fr.openwide.core.imports.table.common.mapping.column.builder.state;

import java.math.BigDecimal;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.builder.function.generic.GenericBigDecimalFunctionBuildStateImpl;
import fr.openwide.core.imports.table.common.mapping.AbstractTableImportColumnSet;

public abstract class BigDecimalState<TTable, TRow, TCell, TCellReference> extends GenericBigDecimalFunctionBuildStateImpl
		<
		AbstractTableImportColumnSet<TTable, TRow, TCell, TCellReference>.Column<BigDecimal>,
		ColumnFunctionBuildStateSwitcher<TTable, TRow, TCell, TCellReference, BigDecimal>,
		BooleanState<TTable, TRow, TCell, TCellReference>,
		DateState<TTable, TRow, TCell, TCellReference>,
		IntegerState<TTable, TRow, TCell, TCellReference>,
		LongState<TTable, TRow, TCell, TCellReference>,
		DoubleState<TTable, TRow, TCell, TCellReference>,
		BigDecimalState<TTable, TRow, TCell, TCellReference>,
		StringState<TTable, TRow, TCell, TCellReference>
		>
		implements ColumnFunctionBuildState<TTable, TRow, TCell, TCellReference, BigDecimal> {
		
	@Override
	public <TValue> GenericState<TTable, TRow, TCell, TCellReference, TValue> transform(Function<? super BigDecimal, TValue> function) {
		return getStateSwitcher().toGeneric(function);
	}

}
