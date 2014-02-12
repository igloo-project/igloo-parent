package fr.openwide.core.imports.excel.mapping.column.builder;

import fr.openwide.core.imports.excel.mapping.column.IExcelImportColumnDefinition;

public interface ColumnBuildCallback<TSheet, TRow, TCell> {
	
	<TValue> void onBuild(IExcelImportColumnDefinition<TSheet, TRow, TCell, TValue> columnDefinition);
	
}