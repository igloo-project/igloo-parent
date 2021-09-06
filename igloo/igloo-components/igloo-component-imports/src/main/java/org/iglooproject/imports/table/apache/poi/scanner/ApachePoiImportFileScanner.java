package org.iglooproject.imports.table.apache.poi.scanner;

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

import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.iglooproject.functional.Predicate2;
import org.iglooproject.functional.Predicates2;
import org.iglooproject.imports.table.apache.poi.location.ApachePoiImportNavigator;
import org.iglooproject.imports.table.common.event.exception.TableImportException;
import org.iglooproject.imports.table.common.event.exception.TableImportFileException;
import org.iglooproject.imports.table.common.excel.scanner.IExcelImportFileScanner;

import com.google.common.collect.ImmutableMap;

import net.java.truevfs.access.TFileInputStream;

public class ApachePoiImportFileScanner implements IExcelImportFileScanner<Workbook, Sheet, Row, Cell, CellReference> {
	
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
	
	protected static final Map<SheetSelection, ? extends Predicate2<? super Sheet>> SELECTIONS_PREDICATES = ImmutableMap.of(
			SheetSelection.ALL, Predicates2.<Sheet>alwaysTrue(),
			SheetSelection.NON_HIDDEN_ONLY, sheet -> {
				if (sheet == null) {
					return false;
				}
				Workbook workbook = sheet.getWorkbook();
				return !workbook.isSheetHidden(workbook.getSheetIndex(sheet));
			}
	);

	@Override
	public void scanRecursively(File file, String filename, SheetSelection selection, IExcelImportFileVisitor<Workbook, Sheet, Row, Cell, CellReference> visitor) throws TableImportException {
		Validate.notNull(file, "file must not be null");
		Validate.notNull(visitor, "visitor must not be null");
		
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
	public void scan(File file, String filename, SheetSelection selection, IExcelImportFileVisitor<Workbook, Sheet, Row, Cell, CellReference> visitor) throws TableImportException {
		Validate.notNull(file, "file must not be null");
		Validate.notNull(visitor, "visitor must not be null");
		
		ApachePoiImportNavigator navigator = new ApachePoiImportNavigator(filename);
		
		try (InputStream stream = new TFileInputStream(file); Workbook workbook = WorkbookFactory.create(stream)) {
			for (int index = 0 ; index < workbook.getNumberOfSheets() ; ++index) {
				Sheet sheet = workbook.getSheetAt(index);
				if (navigator.tableHasContent(sheet) && SELECTIONS_PREDICATES.get(selection).test(sheet)) {
					visitor.visitSheet(navigator, workbook, sheet);
				}
			}
		} catch (IOException | IllegalArgumentException e) {
			throw new TableImportFileException(e, navigator.getLocation(null, null, null));
		}
	}

	@Override
	public void scan(InputStream stream, String filename, SheetSelection selection, IExcelImportFileVisitor<Workbook, Sheet, Row, Cell, CellReference> visitor) throws TableImportException {
		Validate.notNull(stream, "stream must not be null");
		Validate.notNull(visitor, "visitor must not be null");
		
		ApachePoiImportNavigator navigator = new ApachePoiImportNavigator(filename);
		try (Workbook workbook = WorkbookFactory.create(stream)) {
			for (int index = 0 ; index < workbook.getNumberOfSheets() ; ++index) {
				Sheet sheet = workbook.getSheetAt(index);
				if (navigator.tableHasContent(sheet) && SELECTIONS_PREDICATES.get(selection).test(sheet)) {
					visitor.visitSheet(navigator, workbook, sheet);
				}
			}
		} catch (IOException | IllegalArgumentException e) {
			throw new TableImportFileException(e, navigator.getLocation(null, null, null));
		}
	}

}
