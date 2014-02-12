package fr.openwide.core.imports.excel.mapping.column.builder.state;

import fr.openwide.core.commons.util.functional.builder.function.generic.GenericIntegerFunctionBuildStateImpl;
import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;

public abstract class IntegerState<TSheet, TRow, TCell> extends GenericIntegerFunctionBuildStateImpl
		<
		AbstractExcelImportColumnSet<TSheet, TRow, TCell>.Column<Integer>,
		BooleanState<TSheet, TRow, TCell>,
		DateState<TSheet, TRow, TCell>,
		IntegerState<TSheet, TRow, TCell>,
		StringState<TSheet, TRow, TCell>
		> {

}
