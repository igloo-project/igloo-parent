package org.iglooproject.imports.table.apache.poi.location;

import com.google.common.collect.Streams;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.iglooproject.imports.table.apache.poi.util.ApachePoiImportUtils;
import org.iglooproject.imports.table.common.location.ITableImportNavigator;
import org.iglooproject.imports.table.common.location.TableImportLocation;

public class ApachePoiImportNavigator
    implements ITableImportNavigator<Sheet, Row, Cell, CellReference> {

  private String fileName;

  public ApachePoiImportNavigator(String fileName) {
    super();
    this.fileName = fileName;
  }

  @Override
  public boolean tableHasContent(Sheet sheet) {
    // Use rowIterator() instead of iterator() in order to only consider the rows that are actually
    // defined
    return sheet.rowIterator().hasNext();
  }

  @Override
  public boolean rowHasContent(Row row) {
    if (row == null) {
      return false;
    }

    // Use cellIterator() instead of iterator() in order to only consider the cells that are
    // actually defined
    Iterator<Cell> iterator = row.cellIterator();
    while (iterator.hasNext()) {
      if (cellHasContent(iterator.next())) {
        return true;
      }
    }

    return false;
  }

  protected boolean cellHasContent(Cell cell) {
    switch (ApachePoiImportUtils.getCellActualValueType(cell)) {
      case BLANK:
        return false;
      case STRING:
        return !StringUtils.isEmpty(cell.getStringCellValue());
      default:
        // In other cases we cannot detect whether the cell is actually empty in the
        // underlying file, or at least not without relying on low-level APIs.
        // Just give up.
        return true;
    }
  }

  @Override
  public Iterator<Row> rows(Sheet sheet) {
    return sheet.iterator();
  }

  @Override
  public Iterator<Row> nonEmptyRows(Sheet sheet) {
    return Streams.stream(sheet.iterator()).filter(row -> rowHasContent(row)).iterator();
  }

  @Override
  public TableImportLocation getLocation(Sheet sheet, Row row, CellReference cellReference) {
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
    return new TableImportLocation(fileName, sheetName, rowIndex, cellAddress);
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
