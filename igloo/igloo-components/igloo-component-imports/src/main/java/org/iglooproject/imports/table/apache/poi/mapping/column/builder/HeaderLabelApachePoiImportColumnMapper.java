package org.iglooproject.imports.table.apache.poi.mapping.column.builder;

import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.iglooproject.functional.Function2;
import org.iglooproject.functional.Predicate2;
import org.iglooproject.imports.table.common.event.ITableImportEventHandler;
import org.iglooproject.imports.table.common.event.exception.TableImportHeaderLabelMappingException;
import org.iglooproject.imports.table.common.location.ITableImportNavigator;
import org.iglooproject.imports.table.common.mapping.column.builder.ITableImportColumnMapper;
import org.iglooproject.imports.table.common.mapping.column.builder.MappingConstraint;

/*package*/ class HeaderLabelApachePoiImportColumnMapper
    implements ITableImportColumnMapper<Sheet, Row, Cell, CellReference> {

  private final String expectedHeaderLabel;

  private final Predicate2<? super String> predicate;

  private final int indexAmongMatchedColumns;

  private final MappingConstraint mappingConstraint;

  /**
   * @param indexAmongMatchedColumns The 0-based index of this column among the columns matching the
   *     given <code>predicate</code>.
   */
  public HeaderLabelApachePoiImportColumnMapper(
      String expectedHeaderLabel,
      Predicate2<? super String> predicate,
      int indexAmongMatchedColumns,
      MappingConstraint mappingConstraint) {
    super();
    Validate.notNull(predicate, "predicate must not be null");

    this.expectedHeaderLabel = expectedHeaderLabel;
    this.predicate = predicate;
    this.indexAmongMatchedColumns = indexAmongMatchedColumns;
    this.mappingConstraint = mappingConstraint;
  }

  @Override
  public Function2<? super Row, CellReference> tryMap(
      Sheet sheet,
      ITableImportNavigator<Sheet, Row, Cell, CellReference> navigator,
      ITableImportEventHandler eventHandler)
      throws TableImportHeaderLabelMappingException {
    int matchedColumnsCount = 0;
    Row headersRow = sheet.getRow(sheet.getFirstRowNum());

    if (headersRow != null) {
      Iterator<Cell> iterator = headersRow.cellIterator();

      while (iterator.hasNext()) {
        Cell cell = iterator.next();
        String cellValue = StringUtils.trimToNull(cell.getStringCellValue());
        if (predicate.test(cellValue)) {
          if (matchedColumnsCount == indexAmongMatchedColumns) {
            return row ->
                row == null ? null : new CellReference(row.getRowNum(), cell.getColumnIndex());
          } else {
            ++matchedColumnsCount;
          }
        }
      }
    }

    // Could not map the header to a column index
    if (MappingConstraint.REQUIRED.equals(mappingConstraint)) {
      eventHandler.headerLabelMappingError(
          expectedHeaderLabel,
          indexAmongMatchedColumns,
          navigator.getLocation(sheet, headersRow, null));
    }

    return null;
  }
}
