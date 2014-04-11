package fr.openwide.core.imports.excel.poi.location;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import com.google.common.collect.Iterators;

import fr.openwide.core.commons.util.functional.SerializablePredicate;
import fr.openwide.core.imports.excel.location.ExcelImportLocation;
import fr.openwide.core.imports.excel.location.IExcelImportNavigator;

public class ApachePoiExcelImportNavigator implements IExcelImportNavigator<Sheet, Row, Cell, CellReference> {
	
	private String fileName;

	public ApachePoiExcelImportNavigator(String fileName) {
		super();
		this.fileName = fileName;
	}
	
	@Override
	public boolean sheetHasContent(Sheet sheet) {
		return sheet.getRow(sheet.getFirstRowNum()) != null;
	}
	
	@Override
	public boolean rowHasContent(Row row) {
		if (row == null) {
			return false;
		}
		
		for (Cell cell : row) {
			if (cellHasContent(cell)) {
				return true;
			}
		}
		
		return false;
	}
	
	protected boolean cellHasContent(Cell cell) {
		String value = cell.getStringCellValue();
		if (!StringUtils.isEmpty(value)) {
			return true;
		}
		return false;
	}
	
	@Override
	public Iterator<Row> rows(Sheet sheet) {
		return sheet.iterator();
	}
	
	@Override
	public Iterator<Row> nonEmptyRows(Sheet sheet) {
		return Iterators.filter(sheet.iterator(), new SerializablePredicate<Row>() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean apply(Row row) {
				return rowHasContent(row);
			}
		});
	}

	@Override
	public ExcelImportLocation getLocation(Sheet sheet, Row row, CellReference cellReference) {
		String sheetName = null;
		Integer rowIndex = null;
		String cellAddress = null;
		if (sheet != null) {
			sheetName = sheet.getSheetName();
		}
		if (row != null) {
			rowIndex = row.getRowNum();
		}
		if (cellReference != null) {
			cellAddress = cellReference.formatAsString();
		}
		return new ExcelImportLocation(fileName, sheetName, rowIndex, cellAddress);
	}

	@Override
	public Cell getCell(Sheet sheet, CellReference cellReference) {
		if (cellReference == null) {
			return null;
		}
		Row row = sheet.getRow(cellReference.getRow());
		if (row == null) {
			return null;
		}
		return row.getCell(cellReference.getCol());
	}

}
