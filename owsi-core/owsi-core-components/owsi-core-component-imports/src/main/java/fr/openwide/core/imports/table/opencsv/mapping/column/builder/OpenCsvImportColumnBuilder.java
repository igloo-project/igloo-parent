package fr.openwide.core.imports.table.opencsv.mapping.column.builder;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.primitives.Doubles;

import fr.openwide.core.imports.table.common.mapping.AbstractTableImportColumnSet;
import fr.openwide.core.imports.table.common.mapping.column.builder.AbstractTableImportColumnBuilder;
import fr.openwide.core.imports.table.common.mapping.column.builder.ITableImportColumnMapper;
import fr.openwide.core.imports.table.common.mapping.column.builder.MappingConstraint;
import fr.openwide.core.imports.table.common.mapping.column.builder.state.BigDecimalState;
import fr.openwide.core.imports.table.common.mapping.column.builder.state.DateState;
import fr.openwide.core.imports.table.common.mapping.column.builder.state.DoubleState;
import fr.openwide.core.imports.table.common.mapping.column.builder.state.IntegerState;
import fr.openwide.core.imports.table.common.mapping.column.builder.state.LongState;
import fr.openwide.core.imports.table.common.mapping.column.builder.state.StringState;
import fr.openwide.core.imports.table.common.mapping.column.builder.state.TypeState;
import fr.openwide.core.imports.table.opencsv.model.CsvCell;
import fr.openwide.core.imports.table.opencsv.model.CsvCellReference;
import fr.openwide.core.imports.table.opencsv.model.CsvRow;
import fr.openwide.core.imports.table.opencsv.model.CsvTable;

public class OpenCsvImportColumnBuilder extends AbstractTableImportColumnBuilder<CsvTable, CsvRow, CsvCell, CsvCellReference> {
	
	private static final Function<String, Double> DOUBLE_FORMAT_FUNCTION = Doubles.stringConverter();
	
	private static final Function<String, BigDecimal> BIG_DECIMAL_FORMAT_FUNCTION = new Function<String, BigDecimal>() {
		@Override
		public BigDecimal apply(String input) {
			return input == null ? null : new BigDecimal(input);
		}
	};
	
	private final Function<? super String, ? extends Date> dateFormatFunction;
	
	public OpenCsvImportColumnBuilder(Function<? super String, ? extends Date> dateFormatFunction) {
		super();
		this.dateFormatFunction = dateFormatFunction;
	}

	@Override
	public OpenCsvTypeState withHeader(AbstractTableImportColumnSet<CsvTable, CsvRow, CsvCell, CsvCellReference> columnSet, String headerLabel,
			Predicate<? super String> predicate, int indexAmongMatchedColumns, MappingConstraint mappingConstraint) {
		return new OpenCsvTypeState(columnSet, new HeaderLabelOpenCsvImportColumnMapper(headerLabel, predicate, indexAmongMatchedColumns, mappingConstraint));
	}

	@Override
	public OpenCsvTypeState withIndex(AbstractTableImportColumnSet<CsvTable, CsvRow, CsvCell, CsvCellReference> columnSet, int columnIndex) {
		return new OpenCsvTypeState(columnSet, new StaticIndexOpenCsvImportColumnMapper(columnIndex));
	}

	@Override
	public OpenCsvTypeState unmapped(AbstractTableImportColumnSet<CsvTable, CsvRow, CsvCell, CsvCellReference> columnSet) {
		return new OpenCsvTypeState(columnSet, new UnmappableOpenCsvImportColumnMapper());
	}
	
	private class OpenCsvTypeState extends TypeState<CsvTable, CsvRow, CsvCell, CsvCellReference> {

		public OpenCsvTypeState(AbstractTableImportColumnSet<CsvTable, CsvRow, CsvCell, CsvCellReference> columnSet,
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
		public StringState<CsvTable, CsvRow, CsvCell, CsvCellReference> asString(final Supplier<? extends NumberFormat> formatIfNumeric) {
			return new TypeStateSwitcher<CsvCell>(Functions.<CsvCell>identity()).toString(new Function<CsvCell, String>() {
				@Override
				public String apply(CsvCell cell) {
					if (cell == null) {
						return null;
					}
					
					return cell.getContent();
				}
			});
		}

		@Override
		public DateState<CsvTable, CsvRow, CsvCell, CsvCellReference> asDate() {
			return asString().toDate(dateFormatFunction);
		}
	}

}
