package fr.openwide.core.imports.excel.mapping.column.builder.state;

import java.util.Date;

import fr.openwide.core.commons.util.functional.builder.function.generic.GenericDateBuildStateImpl;
import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;

public abstract class DateState<TSheet, TRow, TCell, TCellReference> extends GenericDateBuildStateImpl
		<
		AbstractExcelImportColumnSet<TSheet, TRow, TCell, TCellReference>.Column<Date>,
		BooleanState<TSheet, TRow, TCell, TCellReference>,
		DateState<TSheet, TRow, TCell, TCellReference>,
		IntegerState<TSheet, TRow, TCell, TCellReference>,
		StringState<TSheet, TRow, TCell, TCellReference>
		> {

}
