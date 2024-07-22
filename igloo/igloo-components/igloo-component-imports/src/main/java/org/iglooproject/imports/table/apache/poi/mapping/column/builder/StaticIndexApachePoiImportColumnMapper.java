package org.iglooproject.imports.table.apache.poi.mapping.column.builder;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.iglooproject.functional.Function2;
import org.iglooproject.imports.table.common.event.ITableImportEventHandler;
import org.iglooproject.imports.table.common.location.ITableImportNavigator;
import org.iglooproject.imports.table.common.mapping.column.builder.ITableImportColumnMapper;

/*package*/ class StaticIndexApachePoiImportColumnMapper
    implements ITableImportColumnMapper<Sheet, Row, Cell, CellReference> {

  private final int columnIndex;

  public StaticIndexApachePoiImportColumnMapper(int columnIndex) {
    super();

    this.columnIndex = columnIndex;
  }

  @Override
  public Function2<? super Row, CellReference> tryMap(
      Sheet sheet,
      ITableImportNavigator<Sheet, Row, Cell, CellReference> navigator,
      ITableImportEventHandler eventHandler) {
    return row -> row == null ? null : new CellReference(row.getRowNum(), columnIndex);
  }
}
