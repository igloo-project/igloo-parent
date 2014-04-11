package fr.openwide.core.imports.excel.mapping.column.builder;

import com.google.common.base.Predicate;

import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;
import fr.openwide.core.imports.excel.mapping.column.builder.state.TypeState;

public abstract class AbstractColumnBuilder<TSheet, TRow, TCell, TCellReference> {
	
	public abstract TypeState<TSheet, TRow, TCell, TCellReference> withHeader(
			AbstractExcelImportColumnSet<TSheet, TRow, TCell, TCellReference> columnSet,
			String headerLabel, Predicate<? super String> predicate, int indexAmongMatchedColumns, boolean optional
	);
	
	public abstract TypeState<TSheet, TRow, TCell, TCellReference> withIndex(AbstractExcelImportColumnSet<TSheet, TRow, TCell, TCellReference> columnSet, int index);
	
	public abstract TypeState<TSheet, TRow, TCell, TCellReference> unmapped(AbstractExcelImportColumnSet<TSheet, TRow, TCell, TCellReference> columnSet);

}
