package fr.openwide.core.imports.excel.mapping.column.builder;

import com.google.common.base.Function;

import fr.openwide.core.imports.excel.event.IExcelImportEventHandler;
import fr.openwide.core.imports.excel.event.exception.ExcelImportMappingException;
import fr.openwide.core.imports.excel.location.IExcelImportNavigator;

public interface IExcelImportColumnMapper<TSheet, TRow, TCell, TCellReference> {
	
	/**
	 * @return The row to cellReference mapping function, or null if the mapper was unable to determine a mapping.
	 */
	Function<? super TRow, ? extends TCellReference> tryMap(
			TSheet sheet,
			IExcelImportNavigator<TSheet, TRow, TCell, TCellReference> navigator,
			IExcelImportEventHandler eventHandler
			) throws ExcelImportMappingException;

}
