package fr.openwide.core.imports.excel.mapping.column.builder.state;

import fr.openwide.core.commons.util.functional.builder.function.generic.GenericStringFunctionBuildStateImpl;
import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;

public abstract class StringState<TSheet, TRow, TCell> extends GenericStringFunctionBuildStateImpl
		<
		AbstractExcelImportColumnSet<TSheet, TRow, TCell>.Column<String>,
		BooleanState<TSheet, TRow, TCell>,
		DateState<TSheet, TRow, TCell>,
		IntegerState<TSheet, TRow, TCell>,
		StringState<TSheet, TRow, TCell>
		> {

}
