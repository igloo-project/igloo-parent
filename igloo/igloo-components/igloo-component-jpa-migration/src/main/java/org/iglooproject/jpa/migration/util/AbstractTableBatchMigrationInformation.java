package org.iglooproject.jpa.migration.util;

public abstract class AbstractTableBatchMigrationInformation implements IBatchMigrationInformation {
  private static final String SQL_COUNT_ROWS = "SELECT count(*) FROM %1$s";

  @Override
  public final String getSqlCountRows() {
    return String.format(SQL_COUNT_ROWS, getTableName());
  }

  protected abstract String getTableName();
}
