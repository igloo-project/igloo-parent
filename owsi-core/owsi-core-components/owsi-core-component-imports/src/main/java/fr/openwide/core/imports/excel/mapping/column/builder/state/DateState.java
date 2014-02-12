package fr.openwide.core.imports.excel.mapping.column.builder.state;

import java.util.Date;

import fr.openwide.core.commons.util.functional.builder.function.generic.GenericDateBuildStateImpl;
import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;

public abstract class DateState<TSheet, TRow, TCell> extends GenericDateBuildStateImpl
		<
		AbstractExcelImportColumnSet<TSheet, TRow, TCell>.Column<Date>,
		BooleanState<TSheet, TRow, TCell>,
		DateState<TSheet, TRow, TCell>,
		IntegerState<TSheet, TRow, TCell>,
		StringState<TSheet, TRow, TCell>
		> {

}
