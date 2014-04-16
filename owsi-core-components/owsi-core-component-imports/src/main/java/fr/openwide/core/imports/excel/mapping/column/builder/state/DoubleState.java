package fr.openwide.core.imports.excel.mapping.column.builder.state;

import fr.openwide.core.commons.util.functional.builder.function.generic.GenericDoubleFunctionBuildStateImpl;
import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;

public abstract class DoubleState<TSheet, TRow, TCell, TCellReference> extends GenericDoubleFunctionBuildStateImpl
		<
		AbstractExcelImportColumnSet<TSheet, TRow, TCell, TCellReference>.Column<Double>,
		BooleanState<TSheet, TRow, TCell, TCellReference>,
		DateState<TSheet, TRow, TCell, TCellReference>,
		IntegerState<TSheet, TRow, TCell, TCellReference>,
		LongState<TSheet, TRow, TCell, TCellReference>,
		DoubleState<TSheet, TRow, TCell, TCellReference>,
		StringState<TSheet, TRow, TCell, TCellReference>
		> {

}
