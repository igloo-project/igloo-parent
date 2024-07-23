package org.iglooproject.imports.table.opencsv.model;

import java.io.Serializable;

public class CsvCellReference implements Serializable {

  private static final long serialVersionUID = -581552586802644989L;

  private final int rowIndex;

  private final int columnIndex;

  public CsvCellReference(int rowIndex, int columnIndex) {
    super();
    this.rowIndex = rowIndex;
    this.columnIndex = columnIndex;
  }

  public int getRowIndex() {
    return rowIndex;
  }

  public int getColumnIndex() {
    return columnIndex;
  }
}
