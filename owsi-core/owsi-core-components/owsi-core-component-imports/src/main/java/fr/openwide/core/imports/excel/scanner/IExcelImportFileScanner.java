package fr.openwide.core.imports.excel.scanner;

import java.io.File;
import java.io.InputStream;

import fr.openwide.core.imports.excel.event.exception.ExcelImportException;
import fr.openwide.core.imports.excel.location.IExcelImportNavigator;

public interface IExcelImportFileScanner<TWorkbook, TSheet, TRow, TCell> {
	
	interface IExcelImportFileVisitor<TWorkbook, TSheet, TRow, TCell> {
		void visitSheet(IExcelImportNavigator<TSheet, TRow, TCell> navigator, TWorkbook workbook, TSheet sheet) throws ExcelImportException;
	}
	
	void scanRecursively(File file, String filename, IExcelImportFileVisitor<TWorkbook, TSheet, TRow, TCell> visitor) throws ExcelImportException;
	
	void scan(File file, String filename, IExcelImportFileVisitor<TWorkbook, TSheet, TRow, TCell> visitor) throws ExcelImportException;
	
	void scan(InputStream stream, String filename, IExcelImportFileVisitor<TWorkbook, TSheet, TRow, TCell> visitor) throws ExcelImportException;

}
