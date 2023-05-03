package org.iglooproject.imports.excel.test.poi;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.iglooproject.imports.table.apache.poi.mapping.ApachePoiImportColumnSet;
import org.iglooproject.imports.table.apache.poi.scanner.ApachePoiImportFileScanner;
import org.iglooproject.imports.table.common.event.ITableImportEventHandler;
import org.iglooproject.imports.table.common.event.LoggerTableImportEventHandler;
import org.iglooproject.imports.table.common.event.TableImportNonFatalErrorHandling;
import org.iglooproject.imports.table.common.event.exception.TableImportException;
import org.iglooproject.imports.table.common.excel.scanner.IExcelImportFileScanner.IExcelImportFileVisitor;
import org.iglooproject.imports.table.common.excel.scanner.IExcelImportFileScanner.SheetSelection;
import org.iglooproject.imports.table.common.location.ITableImportNavigator;
import org.iglooproject.imports.table.common.mapping.column.builder.MappingConstraint;
import org.javatuples.Quartet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class TestApachePoiExcelImporter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestApachePoiExcelImporter.class);

	private static class Columns extends ApachePoiImportColumnSet {
		final Column<LocalDate> dateColumn = withIndex(0).asLocalDate().build();
		final Column<Boolean> booleanColumn = withIndex(1).asString().toBoolean(input -> "true".equals(input) ? true : false).build();
		final Column<String> stringColumn = withHeader("StringColumn", 2, MappingConstraint.REQUIRED).asString().clean().build();
		final Column<Integer> integerColumn = withHeader("IntegerColumn").asInteger().build();
		final Column<Integer> missingColumn = withHeader("MissingColumn", MappingConstraint.OPTIONAL).asInteger().build();
	}

	private static final Columns COLUMNS = new Columns();
	private static final ApachePoiImportFileScanner SCANNER = new ApachePoiImportFileScanner();

	public List<Quartet<LocalDate, Boolean, String, Integer>> doImport(InputStream stream, String filename) throws TableImportException {
		final List<Quartet<LocalDate, Boolean, String, Integer>> results = Lists.newArrayList();
		
		SCANNER.scan(stream, filename, SheetSelection.ALL, new IExcelImportFileVisitor<Workbook, Sheet, Row, Cell, CellReference>() {
			@Override
			public void visitSheet(ITableImportNavigator<Sheet, Row, Cell, CellReference> navigator, Workbook workbook, Sheet sheet)
					throws TableImportException {
				ITableImportEventHandler eventHandler = new LoggerTableImportEventHandler(TableImportNonFatalErrorHandling.THROW_IMMEDIATELY, LOGGER);
				
				Columns.TableContext sheetContext = COLUMNS.map(sheet, navigator, eventHandler);
				
				assertTrue(sheetContext.column(COLUMNS.dateColumn).exists());
				assertTrue(sheetContext.column(COLUMNS.integerColumn).exists());
				assertTrue(sheetContext.column(COLUMNS.booleanColumn).exists());
				assertFalse(sheetContext.column(COLUMNS.missingColumn).exists());
				
				for (Columns.RowContext rowContext : Iterables.skip(sheetContext, 1)) {
					Quartet<LocalDate, Boolean, String, Integer> result = Quartet.with(
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
