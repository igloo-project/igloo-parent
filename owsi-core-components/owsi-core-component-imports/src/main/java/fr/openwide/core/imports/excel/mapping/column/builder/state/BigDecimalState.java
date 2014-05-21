package fr.openwide.core.imports.excel.mapping.column.builder.state;

import java.math.BigDecimal;

import com.google.common.base.Function;

import fr.openwide.core.commons.util.functional.builder.function.generic.GenericBigDecimalFunctionBuildStateImpl;
import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;

public abstract class BigDecimalState<TSheet, TRow, TCell, TCellReference> extends GenericBigDecimalFunctionBuildStateImpl
		<
		AbstractExcelImportColumnSet<TSheet, TRow, TCell, TCellReference>.Column<BigDecimal>,
		ColumnFunctionBuildStateSwitcher<TSheet, TRow, TCell, TCellReference, BigDecimal>,
		BooleanState<TSheet, TRow, TCell, TCellReference>,
		DateState<TSheet, TRow, TCell, TCellReference>,
		IntegerState<TSheet, TRow, TCell, TCellReference>,
		LongState<TSheet, TRow, TCell, TCellReference>,
		DoubleState<TSheet, TRow, TCell, TCellReference>,
		BigDecimalState<TSheet, TRow, TCell, TCellReference>,
		StringState<TSheet, TRow, TCell, TCellReference>
		>
		implements ColumnFunctionBuildState<TSheet, TRow, TCell, TCellReference, BigDecimal> {
		
	@Override
	public <TValue> GenericState<TSheet, TRow, TCell, TCellReference, TValue> transform(Function<? super BigDecimal, TValue> function) {
		return getStateSwitcher().toGeneric(function);
	}

}
