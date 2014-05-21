package fr.openwide.core.imports.excel.poi.scanner;

import static org.apache.commons.io.filefilter.FileFilterUtils.and;
import static org.apache.commons.io.filefilter.FileFilterUtils.fileFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.nameFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.notFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.or;
import static org.apache.commons.io.filefilter.FileFilterUtils.prefixFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;

import de.schlichtherle.truezip.file.TFileInputStream;
import fr.openwide.core.commons.util.functional.SerializablePredicate;
import fr.openwide.core.imports.excel.event.exception.ExcelImportException;
import fr.openwide.core.imports.excel.event.exception.ExcelImportFileException;
import fr.openwide.core.imports.excel.poi.location.ApachePoiExcelImportNavigator;
import fr.openwide.core.imports.excel.scanner.IExcelImportFileScanner;

public class ApachePoiExcelImportFileScanner implements IExcelImportFileScanner<Workbook, Sheet, Row, Cell, CellReference> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApachePoiExcelImportFileScanner.class);
	
	private static final String HIDDEN_FILE_PREFIX = ".";
	private static final String THUMBS_DB_NAME = "Thumbs.db";

	// Default is to scan all files except: hidden files, files in hidden directories and files named "Thumbs.db".
	public static final FileFilter DEFAULT_DIRECTORY_CHILD_FILTER = notFileFilter(or(
			prefixFileFilter(HIDDEN_FILE_PREFIX),
			and(
				fileFileFilter(),
				nameFileFilter(THUMBS_DB_NAME)
			)
	));
	
	protected FileFilter getDirectoryChildFilter() {
		return DEFAULT_DIRECTORY_CHILD_FILTER;
	}
	
	protected static final Map<SheetSelection, ? extends Predicate<? super Sheet>> SELECTIONS_PREDICATES = ImmutableMap.of(
			SheetSelection.ALL, Predicates.<Sheet>alwaysTrue(),
			SheetSelection.NON_HIDDEN_ONLY, new SerializablePredicate<Sheet>() {
				private static final long serialVersionUID = 1L;
				@Override
				public boolean apply(Sheet sheet) {
					if (sheet == null) {
						return false;
					}
					Workbook workbook = sheet.getWorkbook();
					return !workbook.isSheetHidden(workbook.getSheetIndex(sheet));
				}
			}
	);

	@Override
	public void scanRecursively(File file, String filename, SheetSelection selection, IExcelImportFileVisitor<Workbook, Sheet, Row, Cell, CellReference> visitor) throws ExcelImportException {
		Validate.notNull(file);
		Validate.notNull(visitor);
		
		if (file.isDirectory()) {
			// Recurse
			FileFilter filter = getDirectoryChildFilter();
			for (File child : file.listFiles(filter)) {
				scanRecursively(child, child.getName(), selection, visitor);
			}
		} else {
			scan(file, filename, selection, visitor);
		}
	}

	@Override
	public void scan(File file, String filename, SheetSelection selection, IExcelImportFileVisitor<Workbook, Sheet, Row, Cell, CellReference> visitor) throws ExcelImportException {
		Validate.notNull(file);
		Validate.notNull(visitor);
		
		ApachePoiExcelImportNavigator navigator = new ApachePoiExcelImportNavigator(filename);
		
		InputStream stream = null;
		try {
			stream = new TFileInputStream(file);
			Workbook workbook = WorkbookFactory.create(stream);
			
			for (int index = 0 ; index < workbook.getNumberOfSheets() ; ++index) {
				Sheet sheet = workbook.getSheetAt(index);
				if (navigator.sheetHasContent(sheet) && SELECTIONS_PREDICATES.get(selection).apply(sheet)) {
					visitor.visitSheet(navigator, workbook, sheet);
				}
			}
		} catch (InvalidFormatException e) {
			throw new ExcelImportFileException(e, navigator.getLocation(null, null, null));
		} catch (IOException e) {
			throw new ExcelImportFileException(e, navigator.getLocation(null, null, null));
		} catch (IllegalArgumentException e) {
			throw new ExcelImportFileException(e, navigator.getLocation(null, null, null));
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
					LOGGER.error("Error while closing stream in finally block", e);
				}
			}
		}
	}

	@Override
	public void scan(InputStream stream, String filename, SheetSelection selection, IExcelImportFileVisitor<Workbook, Sheet, Row, Cell, CellReference> visitor) throws ExcelImportException {
		Validate.notNull(stream);
		Validate.notNull(visitor);
		
		ApachePoiExcelImportNavigator navigator = new ApachePoiExcelImportNavigator(filename);
		try {
			Workbook workbook = WorkbookFactory.create(stream);
			
			for (int index = 0 ; index < workbook.getNumberOfSheets() ; ++index) {
				Sheet sheet = workbook.getSheetAt(index);
				if (navigator.sheetHasContent(sheet) && SELECTIONS_PREDICATES.get(selection).apply(sheet)) {
					visitor.visitSheet(navigator, workbook, sheet);
				}
			}
		} catch (InvalidFormatException e) {
			throw new ExcelImportFileException(e, navigator.getLocation(null, null, null));
		} catch (IOException e) {
			throw new ExcelImportFileException(e, navigator.getLocation(null, null, null));
		} catch (IllegalArgumentException e) {
			throw new ExcelImportFileException(e, navigator.getLocation(null, null, null));
		}
	}

}
