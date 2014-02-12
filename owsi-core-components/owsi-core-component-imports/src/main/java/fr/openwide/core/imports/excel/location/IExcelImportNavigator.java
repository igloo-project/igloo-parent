package fr.openwide.core.imports.excel.location;

import java.util.Iterator;

public interface IExcelImportNavigator<TSheet, TRow, TCell> {

	boolean sheetHasContent(TSheet sheet);

	boolean rowHasContent(TRow row);
	
	Iterator<TRow> rows(TSheet sheet);

	Iterator<TRow> nonEmptyRows(TSheet sheet);
	
	ExcelImportLocation getLocation(TSheet sheet, TRow row, TCell cell);

}
