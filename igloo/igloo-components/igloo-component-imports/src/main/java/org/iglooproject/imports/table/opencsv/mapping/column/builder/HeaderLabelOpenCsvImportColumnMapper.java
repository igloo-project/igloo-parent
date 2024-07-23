package org.iglooproject.imports.table.opencsv.mapping.column.builder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.iglooproject.functional.Function2;
import org.iglooproject.functional.Predicate2;
import org.iglooproject.imports.table.common.event.ITableImportEventHandler;
import org.iglooproject.imports.table.common.event.exception.TableImportHeaderLabelMappingException;
import org.iglooproject.imports.table.common.location.ITableImportNavigator;
import org.iglooproject.imports.table.common.mapping.column.builder.ITableImportColumnMapper;
import org.iglooproject.imports.table.common.mapping.column.builder.MappingConstraint;
import org.iglooproject.imports.table.opencsv.model.CsvCell;
import org.iglooproject.imports.table.opencsv.model.CsvCellReference;
import org.iglooproject.imports.table.opencsv.model.CsvRow;
import org.iglooproject.imports.table.opencsv.model.CsvTable;

/*package*/ class HeaderLabelOpenCsvImportColumnMapper
    implements ITableImportColumnMapper<CsvTable, CsvRow, CsvCell, CsvCellReference> {

  private final String expectedHeaderLabel;

  private final Predicate2<? super String> predicate;

  private final int indexAmongMatchedColumns;

  private final MappingConstraint mappingConstraint;

  /**
   * @param indexAmongMatchedColumns The 0-based index of this column among the columns matching the
   *     given <code>predicate</code>.
   */
  public HeaderLabelOpenCsvImportColumnMapper(
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
  public Function2<? super CsvRow, CsvCellReference> tryMap(
      CsvTable sheet,
      ITableImportNavigator<CsvTable, CsvRow, CsvCell, CsvCellReference> navigator,
      ITableImportEventHandler eventHandler)
      throws TableImportHeaderLabelMappingException {
    int matchedColumnsCount = 0;
    CsvRow headersRow = sheet.getRow(0);

    if (headersRow != null) {
      for (CsvCell cell : headersRow) {
        String cellValue = StringUtils.trimToNull(cell.getContent());
        if (predicate.test(cellValue)) {
          if (matchedColumnsCount == indexAmongMatchedColumns) {
            return row ->
                row == null ? null : new CsvCellReference(row.getIndex(), cell.getIndex());
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
