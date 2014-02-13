package fr.openwide.core.imports.excel.location;

import java.util.Iterator;

public interface IExcelImportNavigator<TSheet, TRow, TCell, TCellReference> {

	boolean sheetHasContent(TSheet sheet);

	boolean rowHasContent(TRow row);
	
	Iterator<TRow> rows(TSheet sheet);

	Iterator<TRow> nonEmptyRows(TSheet sheet);
	
	TCell getCell(TSheet sheet, TCellReference cellReference);
	
	ExcelImportLocation getLocation(TSheet sheet, TRow row, TCellReference cell);

}
