package fr.openwide.core.imports.excel.test.poi;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.javatuples.Quartet;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import fr.openwide.core.imports.excel.event.IExcelImportEventHandler;
import fr.openwide.core.imports.excel.event.SimpleExcelImportEventHandler;
import fr.openwide.core.imports.excel.event.exception.ExcelImportException;
import fr.openwide.core.imports.excel.location.IExcelImportNavigator;
import fr.openwide.core.imports.excel.poi.mapping.ApachePoiImportColumnSet;
import fr.openwide.core.imports.excel.poi.scanner.ApachePoiExcelImportFileScanner;
import fr.openwide.core.imports.excel.scanner.IExcelImportFileScanner.IExcelImportFileVisitor;
import fr.openwide.core.imports.excel.scanner.IExcelImportFileScanner.SheetSelection;

public class TestApachePoiExcelImporter {

	private static class Columns extends ApachePoiImportColumnSet {
		final Column<Date> dateColumn = withIndex(0).asDate().build();
		final Column<Boolean> booleanColumn = withIndex(1).asString().toBoolean(new Function<String, Boolean>() {
					@Override
					public Boolean apply(String input) {
						return "true".equals(input) ? true : false;
					}
				}).build();
		final Column<String> stringColumn = withHeader("StringColumn", 2, false).asString().cleaned().build();
		final Column<Integer> integerColumn = withHeader("IntegerColumn").asInteger().build();
	}

	private static final Columns COLUMNS = new Columns();
	private static final ApachePoiExcelImportFileScanner SCANNER = new ApachePoiExcelImportFileScanner();

	public List<Quartet<Date, Boolean, String, Integer>> doImport(InputStream stream, String filename) throws ExcelImportException {
		final List<Quartet<Date, Boolean, String, Integer>> results = Lists.newArrayList();
		
		SCANNER.scan(stream, filename, SheetSelection.ALL, new IExcelImportFileVisitor<Workbook, Sheet, Row, Cell, CellReference>() {
			@Override
			public void visitSheet(IExcelImportNavigator<Sheet, Row, Cell, CellReference> navigator, Workbook workbook, Sheet sheet)
					throws ExcelImportException {
				IExcelImportEventHandler eventHandler = new SimpleExcelImportEventHandler();
				
				Columns.SheetContext sheetContext = COLUMNS.map(sheet, navigator, eventHandler);
				
				for (Columns.RowContext rowContext : Iterables.skip(sheetContext, 1)) {
					Quartet<Date, Boolean, String, Integer> result = Quartet.with(
							rowContext.cell(COLUMNS.dateColumn).get(),
							rowContext.cell(COLUMNS.booleanColumn).get(),
							rowContext.cell(COLUMNS.stringColumn).get(),
							rowContext.cell(COLUMNS.integerColumn).get()
					);
					
					results.add(result);
				}
			}
		});
		
		return results;
	}

}
