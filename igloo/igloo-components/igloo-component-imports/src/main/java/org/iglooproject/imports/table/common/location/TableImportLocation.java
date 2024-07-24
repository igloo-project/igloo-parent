package org.iglooproject.imports.table.common.location;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
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
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof TableImportLocation)) {
      return false;
    }
    TableImportLocation other = (TableImportLocation) obj;
    return new EqualsBuilder()
        .append(fileName, other.fileName)
        .append(tableName, other.tableName)
        .append(rowIndexZeroBased, other.rowIndexZeroBased)
        .append(cellAddress, other.cellAddress)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(fileName)
        .append(tableName)
        .append(rowIndexZeroBased)
        .append(cellAddress)
        .toHashCode();
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
