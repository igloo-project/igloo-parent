package org.iglooproject.imports.table.opencsv.mapping.column.builder;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import org.iglooproject.functional.Function2;
import org.iglooproject.functional.Functions2;
import org.iglooproject.functional.Predicate2;
import org.iglooproject.functional.Supplier2;
import org.iglooproject.imports.table.common.mapping.AbstractTableImportColumnSet;
import org.iglooproject.imports.table.common.mapping.column.builder.AbstractTableImportColumnBuilder;
import org.iglooproject.imports.table.common.mapping.column.builder.ITableImportColumnMapper;
import org.iglooproject.imports.table.common.mapping.column.builder.MappingConstraint;
import org.iglooproject.imports.table.common.mapping.column.builder.state.BigDecimalState;
import org.iglooproject.imports.table.common.mapping.column.builder.state.DateState;
import org.iglooproject.imports.table.common.mapping.column.builder.state.DoubleState;
import org.iglooproject.imports.table.common.mapping.column.builder.state.IntegerState;
import org.iglooproject.imports.table.common.mapping.column.builder.state.LongState;
import org.iglooproject.imports.table.common.mapping.column.builder.state.StringState;
import org.iglooproject.imports.table.common.mapping.column.builder.state.TypeState;
import org.iglooproject.imports.table.opencsv.model.CsvCell;
import org.iglooproject.imports.table.opencsv.model.CsvCellReference;
import org.iglooproject.imports.table.opencsv.model.CsvRow;
import org.iglooproject.imports.table.opencsv.model.CsvTable;

public class OpenCsvImportColumnBuilder
    extends AbstractTableImportColumnBuilder<CsvTable, CsvRow, CsvCell, CsvCellReference> {

  private static final Function2<String, Double> DOUBLE_FORMAT_FUNCTION =
      input -> Double.valueOf(input);

  private static final Function2<String, BigDecimal> BIG_DECIMAL_FORMAT_FUNCTION =
      input -> input == null ? null : new BigDecimal(input);

  private final Function2<? super String, ? extends Date> dateFormatFunction;

  public OpenCsvImportColumnBuilder(Function2<? super String, ? extends Date> dateFormatFunction) {
    super();
    this.dateFormatFunction = dateFormatFunction;
  }

  @Override
  public OpenCsvTypeState withHeader(
      AbstractTableImportColumnSet<CsvTable, CsvRow, CsvCell, CsvCellReference> columnSet,
      String headerLabel,
      Predicate2<? super String> predicate,
      int indexAmongMatchedColumns,
      MappingConstraint mappingConstraint) {
    return new OpenCsvTypeState(
        columnSet,
        new HeaderLabelOpenCsvImportColumnMapper(
            headerLabel, predicate, indexAmongMatchedColumns, mappingConstraint));
  }

  @Override
  public OpenCsvTypeState withIndex(
      AbstractTableImportColumnSet<CsvTable, CsvRow, CsvCell, CsvCellReference> columnSet,
      int columnIndex) {
    return new OpenCsvTypeState(columnSet, new StaticIndexOpenCsvImportColumnMapper(columnIndex));
  }

  @Override
  public OpenCsvTypeState unmapped(
      AbstractTableImportColumnSet<CsvTable, CsvRow, CsvCell, CsvCellReference> columnSet) {
    return new OpenCsvTypeState(columnSet, new UnmappableOpenCsvImportColumnMapper());
  }

  private class OpenCsvTypeState extends TypeState<CsvTable, CsvRow, CsvCell, CsvCellReference> {

    public OpenCsvTypeState(
        AbstractTableImportColumnSet<CsvTable, CsvRow, CsvCell, CsvCellReference> columnSet,
        ITableImportColumnMapper<CsvTable, CsvRow, CsvCell, CsvCellReference> columnMapper) {
      super(columnSet, columnMapper);
    }

    @Override
    public IntegerState<CsvTable, CsvRow, CsvCell, CsvCellReference> asInteger() {
      return asDouble().toInteger();
    }

    @Override
    public LongState<CsvTable, CsvRow, CsvCell, CsvCellReference> asLong() {
      return asDouble().toLong();
    }

    @Override
    public DoubleState<CsvTable, CsvRow, CsvCell, CsvCellReference> asDouble() {
      return asString().stripLineBreaks().clean().toDouble(DOUBLE_FORMAT_FUNCTION);
    }

    @Override
    public BigDecimalState<CsvTable, CsvRow, CsvCell, CsvCellReference> asBigDecimal() {
      return asString().stripLineBreaks().clean().toBigDecimal(BIG_DECIMAL_FORMAT_FUNCTION);
    }

    @Override
    public StringState<CsvTable, CsvRow, CsvCell, CsvCellReference> asString(
        final Supplier2<? extends NumberFormat> formatIfNumeric) {
      return new TypeStateSwitcher<CsvCell>(Functions2.<CsvCell>identity())
          .toString(cell -> cell != null ? cell.getContent() : null);
    }

    @Override
    public DateState<CsvTable, CsvRow, CsvCell, CsvCellReference> asDate() {
      return asString().toDate(dateFormatFunction);
    }
  }
}
