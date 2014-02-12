package fr.openwide.core.imports.excel.mapping.column.builder;

import com.google.common.base.Predicate;

import fr.openwide.core.imports.excel.mapping.AbstractExcelImportColumnSet;
import fr.openwide.core.imports.excel.mapping.column.builder.state.TypeState;

public abstract class AbstractColumnBuilder<TSheet, TRow, TCell> {
	
	public abstract TypeState<TSheet, TRow, TCell> withHeader(
			AbstractExcelImportColumnSet<TSheet, TRow, TCell> columnSet,
			String headerLabel, Predicate<? super String> predicate, int indexAmongMatchedColumns, boolean optional
	);
	
	public abstract TypeState<TSheet, TRow, TCell> withIndex(AbstractExcelImportColumnSet<TSheet, TRow, TCell> columnSet, int index);
	
	public abstract TypeState<TSheet, TRow, TCell> unmapped(AbstractExcelImportColumnSet<TSheet, TRow, TCell> columnSet);

}
