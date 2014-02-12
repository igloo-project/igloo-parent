package fr.openwide.core.imports.excel.mapping.column.builder.state;

import fr.openwide.core.commons.util.functional.builder.function.generic.GenericBooleanFunctionBuildStateImpl;
import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;

public abstract class BooleanState<TSheet, TRow, TCell> extends GenericBooleanFunctionBuildStateImpl
		<
		AbstractExcelImportColumnSet<TSheet, TRow, TCell>.Column<Boolean>,
		BooleanState<TSheet, TRow, TCell>,
		DateState<TSheet, TRow, TCell>,
		IntegerState<TSheet, TRow, TCell>,
		StringState<TSheet, TRow, TCell>
		> {

}
