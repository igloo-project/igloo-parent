package fr.openwide.core.imports.excel.poi.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang.Validate;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import de.schlichtherle.truezip.file.TFileInputStream;
import fr.openwide.core.imports.excel.event.exception.ExcelImportException;
import fr.openwide.core.imports.excel.event.exception.ExcelImportFileException;
import fr.openwide.core.imports.excel.poi.location.ApachePoiExcelImportNavigator;
import fr.openwide.core.imports.excel.scanner.IExcelImportFileScanner;

public class ApachePoiExcelImportFileScanner implements IExcelImportFileScanner<Workbook, Sheet, Row, Cell> {
	
	private static final String HIDDEN_FILE_PREFIX = ".";

	// Default is to scan all files except hidden files and files in hidden directories.
	public static final FileFilter DEFAULT_DIRECTORY_CHILD_FILTER = FileFilterUtils.notFileFilter(FileFilterUtils.prefixFileFilter(HIDDEN_FILE_PREFIX));
	
	protected FileFilter getDirectoryChildFilter() {
		return DEFAULT_DIRECTORY_CHILD_FILTER;
	}

	@Override
	public void scanRecursively(File file, String filename, IExcelImportFileVisitor<Workbook, Sheet, Row, Cell> visitor) throws ExcelImportException {
		Validate.notNull(file);
		Validate.notNull(visitor);
		
		if (file.isDirectory()) {
			// Recurse
			FileFilter filter = getDirectoryChildFilter();
			for (File child : file.listFiles(filter)) {
				scanRecursively(child, child.getName(), visitor);
			}
		} else {
			scan(file, filename, visitor);
		}
	}

	@Override
	public void scan(File file, String filename, IExcelImportFileVisitor<Workbook, Sheet, Row, Cell> visitor) throws ExcelImportException {
		Validate.notNull(file);
		Validate.notNull(visitor);
		
		ApachePoiExcelImportNavigator navigator = new ApachePoiExcelImportNavigator(file.getName());
		
		try (InputStream stream = new TFileInputStream(file)) {
			Workbook workbook = WorkbookFactory.create(stream);
			
			for (int index = 0 ; index < workbook.getNumberOfSheets() ; ++index) {
				Sheet sheet = workbook.getSheetAt(index);
				if (navigator.sheetHasContent(sheet)) {
					visitor.visitSheet(navigator, workbook, sheet);
				}
			}
		} catch (InvalidFormatException | IOException | IllegalArgumentException e) {
			throw new ExcelImportFileException(e, navigator.getLocation(null, null, null));
		}
	}

	@Override
	public void scan(InputStream stream, String filename, IExcelImportFileVisitor<Workbook, Sheet, Row, Cell> visitor) throws ExcelImportException {
		Validate.notNull(stream);
		Validate.notNull(visitor);
		
		ApachePoiExcelImportNavigator navigator = new ApachePoiExcelImportNavigator(null);
		try {
			Workbook workbook = WorkbookFactory.create(stream);
			
			for (int index = 0 ; index < workbook.getNumberOfSheets() ; ++index) {
				Sheet sheet = workbook.getSheetAt(index);
				if (navigator.sheetHasContent(sheet)) {
					visitor.visitSheet(navigator, workbook, sheet);
				}
			}
		} catch (InvalidFormatException | IOException | IllegalArgumentException e) {
			throw new ExcelImportFileException(e, navigator.getLocation(null, null, null));
		}
	}

}
