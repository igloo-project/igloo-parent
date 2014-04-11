package fr.openwide.core.imports.excel.scanner;

import java.io.File;
import java.io.InputStream;

import fr.openwide.core.imports.excel.event.exception.ExcelImportException;
import fr.openwide.core.imports.excel.location.IExcelImportNavigator;

public interface IExcelImportFileScanner<TWorkbook, TSheet, TRow, TCell, TCellReference> {
	
	enum SheetSelection {
		ALL,
		NON_HIDDEN_ONLY
	}
	
	interface IExcelImportFileVisitor<TWorkbook, TSheet, TRow, TCell, TCellReference> {
		void visitSheet(IExcelImportNavigator<TSheet, TRow, TCell, TCellReference> navigator, TWorkbook workbook, TSheet sheet) throws ExcelImportException;
	}
	
	void scanRecursively(File file, String filename, SheetSelection selection, IExcelImportFileVisitor<TWorkbook, TSheet, TRow, TCell, TCellReference> visitor) throws ExcelImportException;
	
	void scan(File file, String filename, SheetSelection selection, IExcelImportFileVisitor<TWorkbook, TSheet, TRow, TCell, TCellReference> visitor) throws ExcelImportException;
	
	void scan(InputStream stream, String filename, SheetSelection selection, IExcelImportFileVisitor<TWorkbook, TSheet, TRow, TCell, TCellReference> visitor) throws ExcelImportException;

}
