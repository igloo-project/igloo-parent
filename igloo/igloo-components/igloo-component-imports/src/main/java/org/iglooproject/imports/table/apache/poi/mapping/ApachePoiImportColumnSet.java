package org.iglooproject.imports.table.apache.poi.mapping;

import java.util.Comparator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.iglooproject.imports.table.apache.poi.mapping.column.builder.ApachePoiImportColumnBuilder;
import org.iglooproject.imports.table.common.mapping.AbstractTableImportColumnSet;
import org.iglooproject.imports.table.common.mapping.column.builder.AbstractTableImportColumnBuilder;

public class ApachePoiImportColumnSet
    extends AbstractTableImportColumnSet<Sheet, Row, Cell, CellReference> {

  public ApachePoiImportColumnSet() {
    super(new ApachePoiImportColumnBuilder());
  }

  public ApachePoiImportColumnSet(
      AbstractTableImportColumnBuilder<Sheet, Row, Cell, CellReference> builder) {
    super(builder);
  }

  public ApachePoiImportColumnSet(Comparator<? super String> defaultHeaderLabelCollator) {
    super(new ApachePoiImportColumnBuilder(), defaultHeaderLabelCollator);
  }

  public ApachePoiImportColumnSet(
      AbstractTableImportColumnBuilder<Sheet, Row, Cell, CellReference> builder,
      Comparator<? super String> defaultHeaderLabelCollator) {
    super(builder, defaultHeaderLabelCollator);
  }
}
