package fr.openwide.core.imports.csv.location;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Iterators;

import fr.openwide.core.commons.util.functional.SerializablePredicate;
import fr.openwide.core.imports.csv.model.CsvCell;
import fr.openwide.core.imports.csv.model.CsvCellReference;
import fr.openwide.core.imports.csv.model.CsvRow;
import fr.openwide.core.imports.csv.model.CsvSheet;
import fr.openwide.core.imports.excel.location.ExcelImportLocation;
import fr.openwide.core.imports.excel.location.IExcelImportNavigator;

public class OpenCsvImportNavigator implements IExcelImportNavigator<CsvSheet, CsvRow, CsvCell, CsvCellReference> {
	
	private String fileName;

	public OpenCsvImportNavigator(String fileName) {
		super();
		this.fileName = fileName;
	}
	
	@Override
	public boolean sheetHasContent(CsvSheet sheet) {
		return !sheet.getContent().isEmpty();
	}
	
	@Override
	public boolean rowHasContent(CsvRow row) {
		if (row == null) {
			return false;
		}
		
		for (CsvCell cell : row) {
			if (cellHasContent(cell)) {
				return true;
			}
		}
		
		return false;
	}
	
	protected boolean cellHasContent(CsvCell cell) {
		return cell != null && !StringUtils.isEmpty(cell.getContent());
	}
	
	@Override
	public Iterator<CsvRow> rows(CsvSheet sheet) {
		return sheet.iterator();
	}
	
	@Override
	public Iterator<CsvRow> nonEmptyRows(CsvSheet sheet) {
		return Iterators.filter(rows(sheet), new SerializablePredicate<CsvRow>() {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean apply(CsvRow row) {
				return rowHasContent(row);
			}
		});
	}

	@Override
	public ExcelImportLocation getLocation(CsvSheet sheet, CsvRow row, CsvCellReference cellReference) {
		Integer rowIndex = null;
		String cellAddress = null;
		if (row != null) {
			rowIndex = row.getIndex();
		}
		if (cellReference != null) {
			cellAddress = "(" + cellReference.getRowIndex() + "," + cellReference.getColumnIndex() + ")";
		}
		return new ExcelImportLocation(fileName, null /* no sheet name */, rowIndex, cellAddress);
	}

	@Override
	public CsvCell getCell(CsvSheet sheet, CsvCellReference cellReference) {
		if (cellReference == null) {
			return null;
		}
		CsvRow row = sheet.getRow(cellReference.getRowIndex());
		if (row == null) {
			return null;
		}
		return row.getCell(cellReference.getColumnIndex());
	}

}
