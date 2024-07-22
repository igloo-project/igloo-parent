package org.iglooproject.imports.table.common.location;

import java.util.Iterator;

public interface ITableImportNavigator<TTable, TRow, TCell, TCellReference> {

  boolean tableHasContent(TTable table);

  boolean rowHasContent(TRow row);

  Iterator<TRow> rows(TTable table);

  Iterator<TRow> nonEmptyRows(TTable table);

  TCell getCell(TTable table, TCellReference cellReference);

  TableImportLocation getLocation(TTable table, TRow row, TCellReference cell);
}
