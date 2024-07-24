package org.iglooproject.imports.table.opencsv.model;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkPositionIndex;

import java.io.Serializable;

public class CsvCell implements Serializable {

  private static final long serialVersionUID = 6841374847525985451L;

  private final CsvRow row;

  private final int index;

  public CsvCell(CsvRow row, int index) {
    super();
    checkNotNull(row);
    checkPositionIndex(index, row.getContent().size());
    this.row = row;
    this.index = index;
  }

  public CsvRow getRow() {
    return row;
  }

  public String getContent() {
    return row.getContent().get(index);
  }

  public int getIndex() {
    return index;
  }
}
