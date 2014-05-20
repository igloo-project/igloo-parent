package fr.openwide.core.imports.csv.scanner;

import java.io.File;
import java.io.InputStream;

import fr.openwide.core.imports.excel.event.exception.ExcelImportException;
import fr.openwide.core.imports.excel.location.IExcelImportNavigator;

public interface ICsvImportFileScanner<TSheet, TRow, TCell, TCellReference> {
	
	interface ICsvImportFileVisitor<TSheet, TRow, TCell, TCellReference> {
		void visitSheet(IExcelImportNavigator<TSheet, TRow, TCell, TCellReference> navigator, TSheet sheet) throws ExcelImportException;
	}
	
	void scanRecursively(File file, String filename, ICsvImportFileVisitor<TSheet, TRow, TCell, TCellReference> visitor) throws ExcelImportException;
	
	void scan(File file, String filename, ICsvImportFileVisitor<TSheet, TRow, TCell, TCellReference> visitor) throws ExcelImportException;
	
	void scan(InputStream stream, String filename, ICsvImportFileVisitor<TSheet, TRow, TCell, TCellReference> visitor) throws ExcelImportException;

}
