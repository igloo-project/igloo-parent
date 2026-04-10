package org.iglooproject.imports.table.common.location;

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class TableImportLocation implements Serializable {
  private static final long serialVersionUID = 6866449558201453287L;

  private final String fileName;
  private final String tableName;
  private final Integer rowIndexZeroBased;
  private final String cellAddress;

  public TableImportLocation(
      String fileName, String tableName, Integer rowIndexZeroBased, String cellAddress) {
    super();
    this.fileName = fileName;
    this.tableName = tableName;
    this.rowIndexZeroBased = rowIndexZeroBased;
    this.cellAddress = cellAddress;
  }

  public String getFileName() {
    return fileName;
  }

  public String getTableName() {
    return tableName;
  }

  public Integer getRowIndexZeroBased() {
    return rowIndexZeroBased;
  }

  public Integer getRowIndexOneBased() {
    return rowIndexZeroBased == null ? null : rowIndexZeroBased + 1;
  }

  public String getCellAddress() {
    return cellAddress;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof TableImportLocation other)) {
      return false;
    }
    return Objects.equals(fileName, other.fileName)
        && Objects.equals(tableName, other.tableName)
        && Objects.equals(rowIndexZeroBased, other.rowIndexZeroBased)
        && Objects.equals(cellAddress, other.cellAddress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fileName, tableName, rowIndexZeroBased, cellAddress);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("fileName", fileName)
        .append("tableName", tableName)
        .append("rowIndex (1-based)", getRowIndexOneBased())
        .append("cellAddress", cellAddress)
        .build();
  }
}
