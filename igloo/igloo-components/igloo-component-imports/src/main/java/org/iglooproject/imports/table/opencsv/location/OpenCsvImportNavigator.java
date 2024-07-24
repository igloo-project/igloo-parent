package org.iglooproject.imports.table.opencsv.location;

import com.google.common.collect.Streams;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import org.iglooproject.imports.table.common.location.ITableImportNavigator;
import org.iglooproject.imports.table.common.location.TableImportLocation;
import org.iglooproject.imports.table.opencsv.model.CsvCell;
import org.iglooproject.imports.table.opencsv.model.CsvCellReference;
import org.iglooproject.imports.table.opencsv.model.CsvRow;
import org.iglooproject.imports.table.opencsv.model.CsvTable;

public class OpenCsvImportNavigator
    implements ITableImportNavigator<CsvTable, CsvRow, CsvCell, CsvCellReference> {

  private String fileName;

  public OpenCsvImportNavigator(String fileName) {
    super();
    this.fileName = fileName;
  }

  @Override
  public boolean tableHasContent(CsvTable sheet) {
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
  public Iterator<CsvRow> rows(CsvTable sheet) {
    return sheet.iterator();
  }

  @Override
  public Iterator<CsvRow> nonEmptyRows(CsvTable sheet) {
    return Streams.stream(rows(sheet)).filter(row -> rowHasContent(row)).iterator();
  }

  @Override
  public TableImportLocation getLocation(
      CsvTable sheet, CsvRow row, CsvCellReference cellReference) {
    Integer rowIndex = null;
    String cellAddress = null;
    if (row != null) {
      rowIndex = row.getIndex();
    }
    if (cellReference != null) {
      cellAddress = "(" + cellReference.getRowIndex() + "," + cellReference.getColumnIndex() + ")";
    }
    return new TableImportLocation(fileName, null /* no sheet name */, rowIndex, cellAddress);
  }

  @Override
  public CsvCell getCell(CsvTable sheet, CsvCellReference cellReference) {
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
