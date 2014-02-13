package fr.openwide.core.imports.excel.mapping.column.builder;

import com.google.common.base.Function;

import fr.openwide.core.imports.excel.event.IExcelImportEventHandler;
import fr.openwide.core.imports.excel.event.exception.ExcelImportMappingException;
import fr.openwide.core.imports.excel.location.IExcelImportNavigator;

public interface IExcelImportColumnMapper<TSheet, TRow, TCell, TCellReference> {
	
	Function<? super TRow, ? extends TCellReference> map(
			TSheet sheet,
			IExcelImportNavigator<TSheet, TRow, TCell, TCellReference> navigator,
			IExcelImportEventHandler eventHandler
			) throws ExcelImportMappingException;

}
